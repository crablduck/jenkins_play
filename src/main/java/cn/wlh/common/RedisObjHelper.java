package cn.wlh.common;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * redis 工具类
 *
 */
@Component
public class RedisObjHelper {


    /**
     * 失效时间默认：3600秒
     */
    public static final int DEFAULT_EXPIRE_TIME = 60 * 60;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 向Redis中插入String类型数据(默认失效时间：3600秒)；
     *
     * @param key    主键
     * @param value  值
     * @param expire 过期时间 默认时间：3600秒
     * @throws Exception
     */
    public void put(String key, Object value, Integer expire) {
        if ((StringUtils.isEmpty(key)) || (StringUtils.isEmpty(value))) {
            throw new RuntimeException("key/value 为空");
        }
        if (expire == null) {
            expire = DEFAULT_EXPIRE_TIME;
        }
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    /**
     * 向Redis中插入String类型数据(永久缓存， 需要手动删除)
     *
     * @param key   主键
     * @param value 值
     * @throws Exception
     */
    public void put(String key, Object value)   {
        if ((StringUtils.isEmpty(key)) || (StringUtils.isEmpty(value))) {
            throw new RuntimeException("key/value 为空");
        }
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 从缓存中获取数据
     *
     * @param key          主键
     * @throws Exception
     */
    public Object get(String key)  {
        if (StringUtils.isEmpty(key)) {
            throw new RuntimeException("key 为空");
        }
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 根据key删除缓存中的数据
     *
     * @param key 主键
     * @throws Exception
     */
    public void remove(String key){
        if (StringUtils.isEmpty(key)) {
            throw new RuntimeException("key 为空");
        }
        redisTemplate.delete(key);
    }

    /**
     * 判断key是否存在
     *
     * @param key 待验证key
     * @return true：存在， false：不存在
     * @throws Exception
     */
    public boolean hasKey(String key) {
        if (StringUtils.isEmpty(key)) {
            throw new RuntimeException("key 为空");
        }
        return redisTemplate.hasKey(key);
    }
    
}
