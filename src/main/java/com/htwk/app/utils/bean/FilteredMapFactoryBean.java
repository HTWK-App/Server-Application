package com.htwk.app.utils.bean;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.config.AbstractFactoryBean;

public class FilteredMapFactoryBean<V> extends AbstractFactoryBean<Map<String, V>> {

	private Map<String, V> input;

	/**
	 * Set the input map.
	 */
	public void setInput(final Map<String, V> input) {
		this.input = input;
	}

	/**
	 * Set the string by which key prefixes will be filtered.
	 */
	public void setKeyFilterPrefix(final String keyFilterPrefix) {
		this.entryFilter = new EntryFilter<String, V>() {

			@Override
			public boolean accept(final Entry<String, V> entry) {
				return entry.getKey().startsWith(keyFilterPrefix);
			}
		};
	}

	public static interface EntryFilter<EK, EV> {

		boolean accept(Map.Entry<EK, EV> entry);
	}

	/**
	 * If a prefix is not enough, you can supply a custom filter.
	 */
	public void setEntryFilter(final EntryFilter<String, V> entryFilter) {
		this.entryFilter = entryFilter;
	}

	private EntryFilter<String, V> entryFilter;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?> getObjectType() {
		return Map.class;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Map<String, V> createInstance() throws Exception {
		final Map<String, V> map = new LinkedHashMap<String, V>();
		for (final Entry<String, V> entry : this.input.entrySet()) {
			if (this.entryFilter == null || this.entryFilter.accept(entry)) {
				map.put(entry.getKey(), entry.getValue());
			}
		}
		return map;
	}

}