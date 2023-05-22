package com.chenle.bulletchat.util;


import com.chenle.commonutils.RedisUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


@Component
public class OnLineCountUtil {
    private static final String VIDEO_ONLINE_COUNT_PREFIX = "video-online";
    private static final String TOTAL_ONLINE_COUNT_KEY = "total-online";
    private static RedisTemplate<String, Object> redisTemplate;

    private OnLineCountUtil(RedisTemplate<String, Object> redisTemplate) {
        OnLineCountUtil.redisTemplate = redisTemplate;
    }

    public static void add(String videoId, String userId) {
        String videoKey = getVideoKey(videoId);
        redisTemplate.opsForSet().add(videoKey, userId);
        redisTemplate.opsForSet().add(TOTAL_ONLINE_COUNT_KEY, userId);
    }

    public static void remove(String videoId, String userId) {
        String videoKey = getVideoKey(videoId);
        redisTemplate.opsForSet().remove(videoKey, userId);
        redisTemplate.opsForSet().remove(TOTAL_ONLINE_COUNT_KEY, userId);
    }

    /**
     * 获取当前视频观看人数
     */
    public static Long getVideoOnLineCount(String videoId, String userId) {
        String videoKey = getVideoKey(videoId);
        return redisTemplate.opsForSet().size(videoKey);
    }

    /**
     * 获取所有正在观看视频的人数
     */
    public static Long getTotalOnLineCount(String videoId, String userId) {
        return redisTemplate.opsForSet().size(VIDEO_ONLINE_COUNT_PREFIX);
    }

    private static String getVideoKey(String videoId) {
        return RedisUtil.buildKey(VIDEO_ONLINE_COUNT_PREFIX, videoId);
    }


}
