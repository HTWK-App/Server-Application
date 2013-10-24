package com.htwk.app.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;

import com.google.common.base.Optional;

public class GuavaCache implements Cache {

	private final String name;

	private final com.google.common.cache.Cache<Object, Optional<Object>> store;

	public GuavaCache(String name, com.google.common.cache.Cache<Object, Optional<Object>> store) {
		this.name = name;
		this.store = store;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public com.google.common.cache.Cache<Object, Optional<Object>> getNativeCache() {
		return this.store;
	}

	@Override
	public ValueWrapper get(Object key) {
		Optional<Object> value = this.store.getIfPresent(key);
		return (value != null ? new SimpleValueWrapper(value.orNull()) : null);
	}

	@Override
	public void put(Object key, Object value) {
		this.store.put(key, Optional.fromNullable(value));
	}

	@Override
	public void evict(Object key) {
		this.store.invalidate(key);
	}

	@Override
	public void clear() {
		this.store.invalidateAll();
	}

}