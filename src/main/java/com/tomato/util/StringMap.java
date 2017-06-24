package com.tomato.util;

import java.util.Map;

/**
 * The interface String map.
 *
 * @param <V> the type parameter
 */
public interface StringMap<V> extends Map<String, V> {

	/**
	 * 默认 key 大小写不敏感， caseInsensitive = true
	 *
	 * @return Returns the caseInsensitive.
	 */
	public boolean isCaseInsensitive();

	/**
	 * 默认 key 大小写不敏感， caseInsensitive = true
	 *
	 * @param caseInsensitive the case insensitive
	 */
	public void setCaseInsensitive(boolean caseInsensitive);

	/**
	 * Contains key boolean.
	 *
	 * @param key the key
	 *
	 * @return boolean
	 */
	public boolean containsKey(String key);

	/**
	 * Remove v.
	 *
	 * @param key the key
	 *
	 * @return v
	 */
	public V remove(String key);

	/**
	 * Get v.
	 *
	 * @param key the key
	 *
	 * @return v
	 */
	public V get(String key);

	/**
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public V put(String key, V value);

	/**
	 * Gets byte.
	 *
	 * @param key the key
	 * @param defaultValue the default value
	 *
	 * @return byte
	 */
	public Byte getByte(String key, Byte defaultValue);

	/**
	 * Gets byte.
	 *
	 * @param key the key
	 *
	 * @return byte
	 */
	public Byte getByte(String key);

	/**
	 * Gets short.
	 *
	 * @param key the key
	 * @param defaultValue the default value
	 *
	 * @return short
	 */
	public Short getShort(String key, Short defaultValue);

	/**
	 * Gets short.
	 *
	 * @param key the key
	 *
	 * @return short
	 */
	public Short getShort(String key);

	/**
	 * Gets integer.
	 *
	 * @param key the key
	 * @param defaultValue the default value
	 *
	 * @return integer
	 */
	public Integer getInteger(String key, Integer defaultValue);

	/**
	 * Gets integer.
	 *
	 * @param key the key
	 *
	 * @return integer
	 */
	public Integer getInteger(String key);

	/**
	 * Gets int.
	 *
	 * @param key the key
	 * @param defaultValue the default value
	 *
	 * @return int
	 */
	public int getInt(String key, int defaultValue);

	/**
	 * Gets int.
	 *
	 * @param key the key
	 *
	 * @return int
	 */
	public int getInt(String key);

	/**
	 * Gets string.
	 *
	 * @param key the key
	 * @param defaultValue the default value
	 *
	 * @return string
	 */
	public String getString(String key, String defaultValue);

	/**
	 * Gets string.
	 *
	 * @param key the key
	 *
	 * @return string
	 */
	public String getString(String key);

	/**
	 * Gets boolean.
	 *
	 * @param key the key
	 * @param defaultValue the default value
	 *
	 * @return boolean
	 */
	public boolean getBoolean(String key, boolean defaultValue);

	/**
	 * Gets boolean.
	 *
	 * @param key the key
	 *
	 * @return boolean
	 */
	public boolean getBoolean(String key);

}