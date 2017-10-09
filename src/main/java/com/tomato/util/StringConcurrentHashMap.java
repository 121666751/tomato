package com.tomato.util;

import java.util.concurrent.ConcurrentHashMap;

public class StringConcurrentHashMap<V> extends ConcurrentHashMap<String, V> implements StringMap<V> {
	private static final long serialVersionUID = 1L;

	/**
	 * Case Sensitive option
	 */
	private boolean caseInsensitive = true;

	/**
	 * Instantiates a new String concurrent hash map.
	 */
	public StringConcurrentHashMap() {
		super();
	}

	/**
	 * Instantiates a new String concurrent hash map.
	 *
	 * @param initialCapacity the initial capacity
	 */
	public StringConcurrentHashMap(int initialCapacity) {
		super(initialCapacity);
	}

	/* (non-Javadoc)
	 * @see com.charpty.util.StringMap#isCaseInsensitive()
	 */
	@Override
	public boolean isCaseInsensitive() {
		return caseInsensitive;
	}

	/* (non-Javadoc)
	 * @see com.charpty.util.StringMap#setCaseInsensitive(boolean)
	 */
	@Override
	public void setCaseInsensitive(boolean caseInsensitive) {
		this.caseInsensitive = caseInsensitive;
	}

	/* (non-Javadoc)
	 * @see com.charpty.util.StringMap#containsKey(java.lang.String)
	 */
	@Override
	public boolean containsKey(String key) {
		if (caseInsensitive && null != key) {
			return super.containsKey(key.toUpperCase());
		} else {
			return super.containsKey(key);
		}
	}

	/**
	 *
	 */
	@Override
	public boolean containsKey(Object key) {
		if (null != key) {
			return containsKey(key.toString());
		} else {
			return super.containsKey(null);
		}
	}

	/* (non-Javadoc)
	 * @see com.charpty.util.StringMap#remove(java.lang.String)
	 */
	@Override
	public V remove(String key) {
		if (caseInsensitive && null != key) {
			return super.remove(key.toUpperCase());
		} else {
			return super.remove(key);
		}
	}

	/**
	 *
	 */
	@Override
	public V remove(Object key) {
		if (null != key) {
			return remove(key.toString());
		} else {
			return super.remove(null);
		}
	}

	/* (non-Javadoc)
	 * @see com.charpty.util.StringMap#get(java.lang.String)
	 */
	@Override
	public V get(String key) {
		if (caseInsensitive && null != key) {
			return super.get(key.toUpperCase());
		} else {
			return super.get(key);
		}
	}

	/**
	 *
	 */
	@Override
	public V get(Object key) {
		if (null != key) {
			return get(key.toString());
		} else {
			return super.get(null);
		}
	}

	/* (non-Javadoc)
	 * @see com.charpty.util.StringMap#put(java.lang.String, V)
	 */
	@Override
	public V put(String key, V value) {
		if (caseInsensitive && null != key) {
			return super.put(key.toUpperCase(), value);
		} else {
			return super.put(key, value);
		}
	}

	/* (non-Javadoc)
	 * @see com.charpty.util.StringMap#getByte(java.lang.String, java.lang.Byte)
	 */
	@Override
	public Byte getByte(String key, Byte defaultValue) {
		return NumberUtil.byteOf(get(key), defaultValue);
	}

	/* (non-Javadoc)
	 * @see com.charpty.util.StringMap#getByte(java.lang.String)
	 */
	@Override
	public Byte getByte(String key) {
		return NumberUtil.byteOf(get(key));
	}

	/* (non-Javadoc)
	 * @see com.charpty.util.StringMap#getShort(java.lang.String, java.lang.Short)
	 */
	@Override
	public Short getShort(String key, Short defaultValue) {
		return NumberUtil.shortOf(get(key), defaultValue);
	}

	/* (non-Javadoc)
	 * @see com.charpty.util.StringMap#getShort(java.lang.String)
	 */
	@Override
	public Short getShort(String key) {
		return NumberUtil.shortOf(get(key));
	}

	/* (non-Javadoc)
	 * @see com.charpty.util.StringMap#getInteger(java.lang.String, java.lang.Integer)
	 */
	@Override
	public Integer getInteger(String key, Integer defaultValue) {
		return NumberUtil.integerOf(get(key), defaultValue);
	}

	/* (non-Javadoc)
	 * @see com.charpty.util.StringMap#getInteger(java.lang.String)
	 */
	@Override
	public Integer getInteger(String key) {
		return NumberUtil.integerOf(get(key));
	}

	/* (non-Javadoc)
	 * @see com.charpty.util.StringMap#getInt(java.lang.String, int)
	 */
	@Override
	public int getInt(String key, int defaultValue) {
		Integer value = NumberUtil.integerOf(get(key), null);
		if (null != value) {
			return value.intValue();
		}
		return defaultValue;
	}

	/* (non-Javadoc)
	 * @see com.charpty.util.StringMap#getInt(java.lang.String)
	 */
	@Override
	public int getInt(String key) {
		Integer value = NumberUtil.integerOf(get(key), null);
		if (null != value) {
			return value.intValue();
		}
		return (0);
	}

	/* (non-Javadoc)
	 * @see com.charpty.util.StringMap#getString(java.lang.String, java.lang.String)
	 */
	@Override
	public String getString(String key, String defaultValue) {
		Object value = get(key);
		if (null == value) {
			return defaultValue;
		} else if (value instanceof String) {
			return (String) value;
		} else {
			return value.toString();
		}
	}

	/* (non-Javadoc)
	 * @see com.charpty.util.StringMap#getString(java.lang.String)
	 */
	@Override
	public String getString(String key) {
		return getString(key, StringUtil.EMPTY);
	}

	/* (non-Javadoc)
	 * @see com.charpty.util.StringMap#getBoolean(java.lang.String, boolean)
	 */
	@Override
	public boolean getBoolean(String key, boolean defaultValue) {
		return BooleanUtil.parseBoolean(get(key), defaultValue);
	}

	/* (non-Javadoc)
	 * @see com.charpty.util.StringMap#getBoolean(java.lang.String)
	 */
	@Override
	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

}
