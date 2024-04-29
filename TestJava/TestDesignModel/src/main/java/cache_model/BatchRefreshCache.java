package cache_model;

import com.beust.jcommander.internal.Lists;
import com.learn.util.JsonUtil;
import com.learn.util.ThreadUtil;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;

/**
 * == 基于Java的缓存实现
 *
 * cache的包含key, value，一个(key, value)对称为entry。
 * 1. 查询缓存: get
 *    1.1 如果命中缓存且缓存不过期, 则 touchLastAccessTime + returnCachedValue;
 *    1.2 否则不命中, 如果是readRemoteSync 则 touchLastAccessTime + returnCachedValue，直读调用 refreshFromRemote(Key key); 要么加入待刷新列表, 等着异步刷新。
 * 2. 后台异步攒批刷新: 主要做停止刷冷entry和批量刷新工作。如果要异步并发刷新, 实现 refreshThreadsNum 即可
 *    2.1 停止刷新策略：某一个key距离上一次访问超过 stopRefreshAfterLastAccessMs 后，则停止刷新并移除entry;
 *    2.2 固定时间间隔，把需要刷新的key最大攒 maxRefreshBatchSize , 批量刷新调用 refreshFromRemote(List<Key> keys)
 * 3. 其他：delete, put
 *
 * </p>
 */
@Slf4j
@Data
@NoArgsConstructor
public abstract class BatchRefreshCache<K, V> {
  @Data
  public static class Config {
    long cacheSizeLimit = 100;
    long expireAfterWriteInMs = 5 * 1000L;

    // refresh config
    long maxRefreshBatchSize = 100;
    int maxRefreshThreadSize = 2;
    long refreshIntervalMs = 5 * 1000L;

    // NOTE: Equivalent to refreshBeforeExpiryInMs
    int randomAdvanceRefreshTimeMs = 1000;
    long stopRefreshAfterLastAccessMs = expireAfterWriteInMs * 10;
  }

  protected Config config;

  Map<K, CacheValue<V>> cacheMap = new ConcurrentHashMap<>();
  volatile boolean running = true;
  Thread refresher = new Thread(this::autoRefreshTask, "autoRefreshTask");
  ConcurrentLinkedQueue<K> refreshBuffer = new ConcurrentLinkedQueue<>();
  ExecutorService workers;

  public BatchRefreshCache(Config config) {
    this.config = config;
  }

  public void start() throws Exception {
    checkConfig();
    refresher.start();
    workers = new ThreadPoolExecutor(config.getMaxRefreshThreadSize(),
      config.getMaxRefreshThreadSize(),
      config.getStopRefreshAfterLastAccessMs(),
      TimeUnit.MILLISECONDS,
      new LinkedBlockingQueue<>()
    );
    log.info("BatchRefreshCache is running, config: {}", JsonUtil.serialize(config, ""));
  }

  void checkConfig() throws Exception {
    StringBuilder errorMsg = new StringBuilder();
    if (config.getRefreshIntervalMs() <= config.getRandomAdvanceRefreshTimeMs()) {
      errorMsg.append("RefreshIntervalMs should be greater than RandomAdvanceRefreshTimeMs");
    }

    if (errorMsg.length() > 0) {
      throw new Exception(errorMsg.toString());
    }
  }

  public void shutdown() {
    running = false;
    ThreadUtil.safeStopThread(refresher, "close");
    ThreadUtil.safeShutdownPool(workers, "close");
    log.info("BatchRefreshCache is closed, config: {}", JsonUtil.serialize(config, ""));
  }

  private void autoRefreshTask() {
    while (running) {
      try {
        // remove cold entries
        cacheMap.keySet().removeIf(key -> cacheMap.get(key).shouldStopRefresh());

        // refresh entries which is going to expire.
        for (Map.Entry<K, CacheValue<V>> entry : cacheMap.entrySet()) {
          if (entry.getValue().isGoingToExpire() && !entry.getValue().shouldStopRefresh()) {
            refreshBuffer.add(entry.getKey());
          }
        }
        while (!refreshBuffer.isEmpty()) {
          List<K> oneBatch = Lists.newLinkedList();
          for (int i = 0; i < config.getMaxRefreshBatchSize(); i++) {
            K key = refreshBuffer.poll();
            if (key == null) {
              break;
            }
            oneBatch.add(key);
          }
          if (!oneBatch.isEmpty()) {
            workers.submit(() -> refreshCacheFromRemoteBatch(oneBatch));
          }
        }

        long advanceMs = randomLong(0, config.getRandomAdvanceRefreshTimeMs());
        sleepTo(getCurrentTimeMs() + config.getRefreshIntervalMs() - advanceMs, "cacheRefreshInterval");
      } catch (Exception ignored) {
        log.error("Refreshing background error", ignored);
      }
    }
  }

  static Random random = new Random();
  public static long randomLong(int min, int max) {
      return random.nextInt(max) + min;
  }

  private void sleepTo(long sleepToMs, String reason) {
    while (sleepToMs > getCurrentTimeMs()) {
      ThreadUtil.safeSleep(Math.min(500, sleepToMs - getCurrentTimeMs()), reason);
    }
  }

  public void put(K key, V value) {
    if (cacheMap.size() > config.getCacheSizeLimit()) {
      evictLeastRecentlyUsed();
    }
    CacheValue<V> entry = cacheMap.getOrDefault(key, new CacheValue<>(value));
    entry.touch(value);
    cacheMap.put(key, entry);
  }

  public void evict(K key) {
    cacheMap.remove(key);
  }

  private void evictLeastRecentlyUsed() {
    K keyToRemove = null;
    long earliestTime = Long.MAX_VALUE;
    for (Map.Entry<K, CacheValue<V>> entry : cacheMap.entrySet()) {
      if (entry.getValue().getLastAccessTimeMs() < earliestTime) {
        earliestTime = entry.getValue().getLastAccessTimeMs();
        keyToRemove = entry.getKey();
      }
    }
    if (keyToRemove != null) {
      cacheMap.remove(keyToRemove);
    }
  }

  public V get(K key) {
    return get(key, true);
  }

  public V get(K key, boolean readRemoteSync) {
    CacheValue<V> cacheValue = cacheMap.get(key);
    if (cacheValue != null && !cacheValue.isExpired()) {
      cacheValue.setLastAccessTimeMs(getCurrentTimeMs());
      return cacheValue.getValue();
    } else if (readRemoteSync) {
      refreshFromRemote(key);
      return get(key, false);
    }
    return null;
  }

  // 刷新逻辑
  public void refreshFromRemote(@NonNull K key) {
    refreshCacheFromRemoteBatch(Lists.newArrayList(key));
  }

  // 批量刷新逻辑
  public void refreshCacheFromRemoteBatch(@NonNull List<K> keys) {
    for (K key : keys) {
      // refresh logic here
      put(key, (V) (key + "test"));
    }
  }

  public long getCurrentTimeMs() {
    return System.currentTimeMillis();
  }

  @Data
  private class CacheValue<V> {
    V value;
    long createTimeMs;
    long expireTimeMs;
    long lastAccessTimeMs;

    public CacheValue(V value) {
      this.lastAccessTimeMs = getCurrentTimeMs();
      touch(value);
    }

    // update cache but not create cache
    public void touch(V value) {
      this.value = value;
      this.createTimeMs = getCurrentTimeMs();
      this.expireTimeMs = createTimeMs + config.getExpireAfterWriteInMs();
    }

    public boolean isExpired() {
      return getCurrentTimeMs() >= expireTimeMs;
    }

    public boolean isGoingToExpire() {
      return getCurrentTimeMs() + config.getRandomAdvanceRefreshTimeMs() >= expireTimeMs;
    }

    boolean shouldStopRefresh() {
      return getCurrentTimeMs() >= lastAccessTimeMs + config.getStopRefreshAfterLastAccessMs();
    }
  }

  public static void main(String[] args) {
    BatchRefreshCache.Config config = new BatchRefreshCache.Config();
    // config.setCacheSizeLimit(100);
    // config.setExpireAfterWriteInMs(6000);
    // config.setStopRefreshAfterLastAccessMs(config.getExpireAfterWriteInMs() * 10);

    // BatchRefreshCache cache = Mockito.spy(BatchRefreshCache.class);
    // cache.setConfig(config);
    // Shutdown the cache when done
  }
}
