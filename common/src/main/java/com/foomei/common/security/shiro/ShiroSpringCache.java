package com.foomei.common.security.shiro;

import org.apache.shiro.cache.CacheException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class ShiroSpringCache<K, V> implements org.apache.shiro.cache.Cache<K, V> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ShiroSpringCache.class);

	private final org.springframework.cache.Cache cache;

	public ShiroSpringCache(@NotNull Cache cache) {
		this.cache = cache;
	}

	public V get(K key) throws CacheException {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Getting object from cache [" + this.cache.getName() + "] for key [" + key + "]key type:" + key.getClass());
		}
		ValueWrapper valueWrapper = cache.get(key);
		if (valueWrapper == null) {
			if (LOGGER.isTraceEnabled()) {
				LOGGER.trace("Element for [" + key + "] is null.");
			}
			return null;
		}
		return (V) valueWrapper.get();
	}

	public V put(K key, V value) throws CacheException {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Putting object in cache [" + this.cache.getName() + "] for key [" + key + "]key type:" + key.getClass());
		}
		V previous = get(key);
		cache.put(key, value);
		return previous;
	}

	public V remove(K key) throws CacheException {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Removing object from cache [" + this.cache.getName() + "] for key [" + key + "]key type:" + key.getClass());
		}
		V previous = get(key);
		cache.evict(key);
		return previous;
	}

	public void clear() throws CacheException {
		if (LOGGER.isTraceEnabled()) {
			LOGGER.trace("Clearing all objects from cache [" + this.cache.getName() + "]");
		}
		cache.clear();
	}

	public int size() {
		return 0;
	}

	public Set<K> keys() {
		return Collections.emptySet();
	}

	public Collection<V> values() {
		return Collections.emptySet();
	}

	@Override
	public String toString() {
		return "ShiroSpringCache [" + this.cache.getName() + "]";
	}

}
