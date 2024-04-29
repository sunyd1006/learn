package cache_model;

import com.learn.util.ThreadUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class BatchRefreshCacheTest {
  @Spy
  private BatchRefreshCache.Config mockConfig;

  @Spy
  private BatchRefreshCache<String, String> cache;

  long currentTimeMs = 1000;

  @BeforeEach
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);

    when(mockConfig.getCacheSizeLimit()).thenReturn(10L);
    when(mockConfig.getExpireAfterWriteInMs()).thenReturn(5 * 1000L);

    when(mockConfig.getMaxRefreshBatchSize()).thenReturn(10L);
    when(mockConfig.getRefreshIntervalMs()).thenReturn(5 * 1000L);

    long expiredTimeMs = mockConfig.getExpireAfterWriteInMs();
    when(mockConfig.getStopRefreshAfterLastAccessMs()).thenReturn(expiredTimeMs * 10);
    when(mockConfig.getMaxRefreshThreadSize()).thenReturn(2);
    mockCurrentTimeMs(currentTimeMs);

    cache.setConfig(mockConfig);
    cache.start();
  }

  @AfterEach
  public void tearDown() {
  }

  void mockCurrentTimeMs(long millis) {
    when(cache.getCurrentTimeMs()).thenReturn(millis);
  }

  void mockRefreshFromRemote(String key, String value) {
    doAnswer(invocation -> {
      cache.put(key, value);
      return null;
    }).when(cache).refreshFromRemote(anyString());
  }

  void mockRefreshFromRemoteBatch(Map<String, String> lists) {
    doAnswer(invocation -> {
      lists.forEach((k, v) -> {
        cache.put(k, v);
      });
      return null;
    }).when(cache).refreshCacheFromRemoteBatch(any());
  }

  @Test
  public void testGetAndPut() {
    // 假设refreshCacheFromRemote返回一个固定值
    mockRefreshFromRemote("key1", "value1");

    // 1. 测试get方法
    mockCurrentTimeMs(currentTimeMs);
    Assertions.assertNull(cache.get("key1", false)); // 直接从缓存获取，应该是null
    Assertions.assertEquals("value1", cache.get("key1")); // 从远程获取并放入缓存
    Assertions.assertEquals("value1", cache.get("key1")); // 现在从缓存获取，应该有值


    // 2. cache expired
    mockRefreshFromRemote("key1", "value2");
    mockCurrentTimeMs(currentTimeMs + mockConfig.getExpireAfterWriteInMs());
    Assertions.assertEquals("value2", cache.get("key1")); // 从远程获取并放入缓存

    // 3. put entry, and entry has been changed
    cache.put("key1", "newValue");
    Assertions.assertEquals("newValue", cache.get("key1")); // 现在获取新的值
  }

  @Test
  public void testDeleteAndRefresh() {
    // 测试移除缓存项
    cache.put("key1", "value1");
    cache.evict("key1");
    Assertions.assertNull(cache.get("key1", false));

    // 测试后台刷新逻辑
    // 做一些设置让缓存项过期，测试自动刷新
    when(mockConfig.getRefreshIntervalMs()).thenReturn(10L); // 设定刷新间隔更短
    when(cache.get("key1")).thenReturn("refreshedValue");

    // 放入一个缓存项然后等待刷新间隔
    cache.put("key1", "value1");

    ThreadUtil.safeSleep(100, "deleteAndRefresh");
    Assertions.assertEquals("refreshedValue", cache.get("key1")); // 确认缓存项被刷新
  }

  @Test
  public void testRefreshAndStopRefresh() {
    mockCurrentTimeMs(currentTimeMs);
    Assertions.assertEquals(currentTimeMs, cache.getCurrentTimeMs());

    cache.put("key1", "value1");
    cache.put("key2", "value2"); // cold
    cache.put("key3", "value3"); // cold

    Map<String, String> newCache = new HashMap<>();
    newCache.put("key1", "value11");
    mockRefreshFromRemoteBatch(newCache);

    // Touch entry and verify it's still available
    mockCurrentTimeMs(currentTimeMs + mockConfig.getExpireAfterWriteInMs() / 2);
    Assertions.assertEquals("value1", cache.get("key1", false));

    mockCurrentTimeMs(currentTimeMs + mockConfig.getStopRefreshAfterLastAccessMs() / 2);
    ThreadUtil.safeSleep(500, "waitRefreshInterval");
    Mockito.verify(cache, atLeast(1)).refreshCacheFromRemoteBatch(argThat(list -> list.contains("key2")));
    Mockito.verify(cache, atLeast(1)).refreshCacheFromRemoteBatch(argThat(list -> list.contains("key1")));

    // Verify no more refresh after last access time exceeds threshold
    mockCurrentTimeMs(currentTimeMs + mockConfig.getStopRefreshAfterLastAccessMs() + 1);
    ThreadUtil.safeSleep(500, "waitRefreshInterval");
    Mockito.verify(cache, atLeast(1)).refreshCacheFromRemoteBatch(argThat(list -> list.contains("key1")));

    // reset batchRefreshCache对应mock对象的之前所有交互，包括对refreshCacheFromRemoteBatch的调用。
    reset(cache);
    Mockito.verify(cache, times(0)).refreshCacheFromRemoteBatch(argThat(list -> list.contains("key2")));
  }
}
