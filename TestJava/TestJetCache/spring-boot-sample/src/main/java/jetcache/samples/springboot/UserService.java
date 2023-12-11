/**
 * Created on 2018/8/11.
 */
package jetcache.samples.springboot;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;

/**
 * @author <a href="mailto:areyouok@gmail.com">huangli</a>
 */
public interface UserService {

    // cacheType default with remote
    @Cached(name = "loadUser", expire = 10, cacheType = CacheType.LOCAL)
    User loadUser(long userId);
}
