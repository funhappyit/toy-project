package com.example.bloghome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import com.example.bloghome.dto.UserProfileDTO;

@Service
public class UserProfileCacheService {

	private static final String USER_PROFILE_KEY_PREFIX = "user_profile:";

	private final RedisTemplate<String, Object> redisTemplate;

	public UserProfileCacheService(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
		this.redisTemplate.setKeySerializer(new StringRedisSerializer());
		this.redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
	}

	public void cacheUserProfile(String username, UserProfileDTO userProfileDTO) {
		redisTemplate.opsForValue().set(USER_PROFILE_KEY_PREFIX + username, userProfileDTO);
	}

	public UserProfileDTO getCachedUserProfile(String username) {
			return (UserProfileDTO) redisTemplate.opsForValue().get(USER_PROFILE_KEY_PREFIX + username);

	}
}
