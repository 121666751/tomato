package com.tomato.util;

/**
 * The type File path.
 */
public final class FilePath extends Path {
	private static final char separator;
	private static final char revSeparator;

	static {
		try {
			// We disable the JIT during class initialization.
			Compiler.disable();
			String value = System.getProperty("file.separator");
			if (null != value && SLASH == value.charAt(0)) {
				separator = SLASH;
				revSeparator = BACKSLASH;
			} else {
				separator = BACKSLASH;
				revSeparator = SLASH;
			}
		} finally {
			// Make sure to always re-enable the JIT.
			Compiler.enable();
		}
	}

	/**
	 * Instantiates a new File path.
	 */
	public FilePath() {
		super();
		setSeparator(separator, revSeparator);
		setAllowDuplicatedSeparator(false);
	}

	/**
	 * Instantiates a new File path.
	 *
	 * @param capacity the capacity
	 */
	public FilePath(int capacity) {
		super(capacity);
		setSeparator(separator, revSeparator);
		setAllowDuplicatedSeparator(false);
	}

	/**
	 * Instantiates a new File path.
	 *
	 * @param p the p
	 */
	public FilePath(String p) {
		super((null != p ? p.length() : 0) + CAPACITY);
		setSeparator(separator, revSeparator);
		setAllowDuplicatedSeparator(false);
		append(p);
	}

}
