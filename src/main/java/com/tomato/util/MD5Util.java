package com.tomato.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;



public final class MD5Util {
	/**
	 * <li/>为兼容已开发的C/S程序，默认使用 GB18030 字符集, SHA1Util 默认使用 UTF-8 字符集
	 * <li/>{@code Charset} 此类中定义的所有方法用于并发线程是安全的。
	 */
	public static final Charset CHARSET_DEFAULT = Charset.forName("GB18030");
	public static final String ALGORITHM_MD5 = "MD5";
	// Store local thread information
	private static final ThreadLocal<MessageDigest> md5Holder = new ThreadLocal<MessageDigest>() {

		/* (non-Javadoc)
		 * @see java.lang.ThreadLocal#initialValue()
		 */
		@Override
		protected MessageDigest initialValue() {
			try {
				return MessageDigest.getInstance(ALGORITHM_MD5);
			} catch (NoSuchAlgorithmException e) {
				throw new RuntimeException(e);
			}
		}

	};

	// Prevent instantiation
	private MD5Util() {
		super();
	}

	/**
	 * 返回线程安全实例，只限当前线程使用
	 * 
	 * @return 永远不会返回null
	 */
	public static MessageDigest getInstance() {
		return md5Holder.get();
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	public static byte[] digest(byte[] input) {
		return getInstance().digest(input);
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	public static byte[] doubleDigest(byte[] input) {
		MessageDigest digest = getInstance();
		return digest.digest(digest.digest(input));
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	public static String hexDigest(byte[] input) {
		return StringUtil.toHexString(digest(input));
	}

	/**
	 * 
	 * @param input
	 * @param charset
	 * @return
	 */
	public static byte[] digest(String input, Charset charset) {
		return digest(input.getBytes(charset));
	}

	/**
	 * 
	 * @param input
	 * @param charsetName
	 * @return
	 */
	public static byte[] digest(String input, String charsetName) {
		try {
			return digest(input.getBytes(charsetName));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @param input
	 * @param charset
	 * @return
	 */
	public static String hexDigest(String input, Charset charset) {
		return StringUtil.toHexString(digest(input, charset));
	}

	/**
	 * 
	 * @param input
	 * @param charsetName
	 * @return
	 */
	public static String hexDigest(String input, String charsetName) {
		return StringUtil.toHexString(digest(input, charsetName));
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	public static byte[] digest(String input) {
		return digest(input, CHARSET_DEFAULT);
	}

	/**
	 * 
	 * @param input
	 * @return
	 */
	public static String hexDigest(String input) {
		return hexDigest(input, CHARSET_DEFAULT);
	}

}
