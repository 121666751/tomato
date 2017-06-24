package com.tomato.util;

import java.util.HashMap;

/**
 * The type String hash map.
 *
 * @param <V> the type parameter
 *
 * @author WuJianqiang
 * @version $Id : com.tomato.util.StringHashMap.java 16594 2016-06-18 12:49:04Z WuJianqiang $
 * @since 2010 -4-21 上午11:11:13
 */
public class StringHashMap<V> extends HashMap<String, V> implements StringMap<V> {
	private static final long serialVersionUID = 1L;

	/** Case Sensitive option */
	private boolean caseInsensitive = true;

	/**
	 * Instantiates a new String hash map.
	 */
	public StringHashMap() {
		super();
	}

	/**
	 * Instantiates a new String hash map.
	 *
	 * @param initialCapacity the initial capacity
	 */
	public StringHashMap(int initialCapacity) {
		super(initialCapacity);
	}

	/* (non-Javadoc)
	 * @see com.ifugle.util.StringMap#isCaseInsensitive()
	 */
	@Override
	public boolean isCaseInsensitive() {
		return caseInsensitive;
	}

	/* (non-Javadoc)
	 * @see com.ifugle.util.StringMap#setCaseInsensitive(boolean)
	 */
	@Override
	public void setCaseInsensitive(boolean caseInsensitive) {
		this.caseInsensitive = caseInsensitive;
	}

	/* (non-Javadoc)
	 * @see com.ifugle.util.StringMap#containsKey(java.lang.String)
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
	 * @see com.ifugle.util.StringMap#remove(java.lang.String)
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
	 * @see com.ifugle.util.StringMap#get(java.lang.String)
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
	 * @see com.ifugle.util.StringMap#put(java.lang.String, V)
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
	 * @see com.ifugle.util.StringMap#getByte(java.lang.String, java.lang.Byte)
	 */
	@Override
	public Byte getByte(String key, Byte defaultValue) {
		return NumberUtil.byteOf(get(key), defaultValue);
	}

	/* (non-Javadoc)
	 * @see com.ifugle.util.StringMap#getByte(java.lang.String)
	 */
	@Override
	public Byte getByte(String key) {
		return NumberUtil.byteOf(get(key));
	}

	/* (non-Javadoc)
	 * @see com.ifugle.util.StringMap#getShort(java.lang.String, java.lang.Short)
	 */
	@Override
	public Short getShort(String key, Short defaultValue) {
		return NumberUtil.shortOf(get(key), defaultValue);
	}

	/* (non-Javadoc)
	 * @see com.ifugle.util.StringMap#getShort(java.lang.String)
	 */
	@Override
	public Short getShort(String key) {
		return NumberUtil.shortOf(get(key));
	}

	/* (non-Javadoc)
	 * @see com.ifugle.util.StringMap#getInteger(java.lang.String, java.lang.Integer)
	 */
	@Override
	public Integer getInteger(String key, Integer defaultValue) {
		return NumberUtil.integerOf(get(key), defaultValue);
	}

	/* (non-Javadoc)
	 * @see com.ifugle.util.StringMap#getInteger(java.lang.String)
	 */
	@Override
	public Integer getInteger(String key) {
		return NumberUtil.integerOf(get(key));
	}

	/* (non-Javadoc)
	 * @see com.ifugle.util.StringMap#getInt(java.lang.String, int)
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
	 * @see com.ifugle.util.StringMap#getInt(java.lang.String)
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
	 * @see com.ifugle.util.StringMap#getString(java.lang.String, java.lang.String)
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
	 * @see com.ifugle.util.StringMap#getString(java.lang.String)
	 */
	@Override
	public String getString(String key) {
		return getString(key, StringUtil.EMPTY);
	}

	/* (non-Javadoc)
	 * @see com.ifugle.util.StringMap#getBoolean(java.lang.String, boolean)
	 */
	@Override
	public boolean getBoolean(String key, boolean defaultValue) {
		return BooleanUtil.parseBoolean(get(key), defaultValue);
	}

	/* (non-Javadoc)
	 * @see com.ifugle.util.StringMap#getBoolean(java.lang.String)
	 */
	@Override
	public boolean getBoolean(String key) {
		return getBoolean(key, false);
	}

}
