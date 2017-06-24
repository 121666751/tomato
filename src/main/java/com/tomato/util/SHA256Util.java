package com.tomato.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * The type Sha 256 util.
 */
public final class SHA256Util {
	/**
	 * <li/>默认使用 UTF-8 字符集, MD5Util 默认使用 GB18030 字符集
	 * <li/>{@code Charset} 此类中定义的所有方法用于并发线程是安全的。
	 */
	public static final Charset CHARSET_DEFAULT = Charset.forName("UTF-8");
	/**
	 * The constant ALGORITHM_SHA256.
	 */
	public static final String ALGORITHM_SHA256 = "SHA-256";
	// Store local thread information
	private static final ThreadLocal<MessageDigest> shaHolder = new ThreadLocal<MessageDigest>() {

		/* (non-Javadoc)
		 * @see java.lang.ThreadLocal#initialValue()
		 */
		@Override
		protected MessageDigest initialValue() {
			try {
				return MessageDigest.getInstance(ALGORITHM_SHA256);
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
		}

	};

	// Prevent instantiation
	private SHA256Util() {
		super();
	}

	/**
	 * 返回线程安全实例，只限当前线程使用
	 *
	 * @return 永远不会返回null instance
	 */
	public static MessageDigest getInstance() {
		return shaHolder.get();
	}

	/**
	 * Digest byte [ ].
	 *
	 * @param input the input
	 *
	 * @return byte [ ]
	 */
	public static byte[] digest(byte[] input) {
		return getInstance().digest(input);
	}

	/**
	 * Double digest byte [ ].
	 *
	 * @param input the input
	 *
	 * @return byte [ ]
	 */
	public static byte[] doubleDigest(byte[] input) {
		MessageDigest digest = getInstance();
		return digest.digest(digest.digest(input));
	}

	/**
	 * Hex digest string.
	 *
	 * @param input the input
	 *
	 * @return string
	 */
	public static String hexDigest(byte[] input) {
		return StringUtil.toHexString(digest(input));
	}

	/**
	 * Digest byte [ ].
	 *
	 * @param input the input
	 * @param charset the charset
	 *
	 * @return byte [ ]
	 */
	public static byte[] digest(String input, Charset charset) {
		return digest(input.getBytes(charset));
	}

	/**
	 * Digest byte [ ].
	 *
	 * @param input the input
	 * @param charsetName the charset name
	 *
	 * @return byte [ ]
	 */
	public static byte[] digest(String input, String charsetName) {
		try {
			return digest(input.getBytes(charsetName));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Hex digest string.
	 *
	 * @param input the input
	 * @param charset the charset
	 *
	 * @return string
	 */
	public static String hexDigest(String input, Charset charset) {
		return StringUtil.toHexString(digest(input, charset));
	}

	/**
	 * Hex digest string.
	 *
	 * @param input the input
	 * @param charsetName the charset name
	 *
	 * @return string
	 */
	public static String hexDigest(String input, String charsetName) {
		return StringUtil.toHexString(digest(input, charsetName));
	}

	/**
	 * Digest byte [ ].
	 *
	 * @param input the input
	 *
	 * @return byte [ ]
	 */
	public static byte[] digest(String input) {
		return digest(input, CHARSET_DEFAULT);
	}

	/**
	 * Hex digest string.
	 *
	 * @param input the input
	 *
	 * @return string
	 */
	public static String hexDigest(String input) {
		return hexDigest(input, CHARSET_DEFAULT);
	}

}
