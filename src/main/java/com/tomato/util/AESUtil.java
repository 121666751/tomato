package com.tomato.util;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;


public class AESUtil {
	private static final Charset CHARSET_DEFAULT = Charset.forName("UTF-8");
	private static final String ALGORITHM_MD5 = "MD5";
	private static final String ALGORITHM_AES = "AES";
	private static final String ALGORITHM_PBKDF2_HMACSHA1 = "PBKDF2WithHmacSHA1";
	private static final String CBC_MODE_PADDING = ALGORITHM_AES + "/CBC/PKCS5Padding";
	private static final int DEFAULT_ITERATIONS = 65536;
	private static final int DEFAULT_HASH_WIDTH = 128;
	private static final Map<String, SecretKey> SECRET_KEYS = new HashMap<>();

	/**
	 * Encrypt with zip to base 64 string.
	 *
	 * @param text the text
	 * @param password the password
	 *
	 * @return string
	 *
	 * @throws Exception the exception
	 */
	public static String encryptWithZipToBase64(String text, String password) throws Exception {
		if (text == null || text.isEmpty()) {
			return text;
		}
		return base64Encode(encrypt(zip(getBytes(text)), password));
	}

	/**
	 * Decrypt with unzip from base 64 string.
	 *
	 * @param base64String the base 64 string
	 * @param password the password
	 *
	 * @return string
	 *
	 * @throws Exception the exception
	 */
	public static String decryptWithUnzipFromBase64(String base64String, String password) throws Exception {
		if (base64String == null || base64String.isEmpty()) {
			return base64String;
		}
		return unzip(decrypt(base64Decode(base64String), password));
	}

	/**
	 * Encrypt to base 64 string.
	 *
	 * @param text the text
	 * @param password the password
	 *
	 * @return string
	 *
	 * @throws Exception the exception
	 */
	public static String encryptToBase64(String text, String password) throws Exception {
		if (text == null || text.isEmpty()) {
			return text;
		}
		return base64Encode(encrypt(getBytes(text), password));
	}

	/**
	 * Decrypt from base 64 string.
	 *
	 * @param base64String the base 64 string
	 * @param password the password
	 *
	 * @return string
	 *
	 * @throws Exception the exception
	 */
	public static String decryptFromBase64(String base64String, String password) throws Exception {
		if (base64String == null || base64String.isEmpty()) {
			return base64String;
		}
		return new String(decrypt(base64Decode(base64String), password), CHARSET_DEFAULT);
	}

	/**
	 * Encrypt byte [ ].
	 *
	 * @param data the data
	 * @param password the password
	 *
	 * @return byte [ ]
	 *
	 * @throws Exception the exception
	 */
	public static byte[] encrypt(byte[] data, String password) throws Exception {
		return crypt(1, data, password);
	}

	/**
	 * Decrypt byte [ ].
	 *
	 * @param data the data
	 * @param password the password
	 *
	 * @return byte [ ]
	 *
	 * @throws Exception the exception
	 */
	public static byte[] decrypt(byte[] data, String password) throws Exception {
		return crypt(2, data, password);
	}

	/**
	 * @param opmode
	 *            1 加密，2 解密
	 * @param data
	 * @param password
	 * @return
	 * @throws Exception
	 */
	private static byte[] crypt(int opmode, byte[] data, String password) throws Exception {
		byte[] salt = getMD5(password);
		IvParameterSpec iv = new IvParameterSpec(salt);

		SecretKey secretKey = SECRET_KEYS.get(password);
		if (secretKey == null) {
			synchronized (SECRET_KEYS) {
				secretKey = SECRET_KEYS.get(password);
				if (secretKey == null) {
					secretKey = getPBESecretKey(password, salt);
					secretKey = new SecretKeySpec(secretKey.getEncoded(), ALGORITHM_AES);
					SECRET_KEYS.put(password, secretKey);
				}
			}
		}

		Cipher cipher = Cipher.getInstance(CBC_MODE_PADDING);
		cipher.init(opmode, secretKey, iv);

		return cipher.doFinal(data);
	}

	/**
	 * Gets pbe secret key.
	 *
	 * @param password the password
	 * @param salt the salt
	 *
	 * @return pbe secret key
	 *
	 * @throws Exception the exception
	 */
	public static SecretKey getPBESecretKey(String password, byte[] salt) throws Exception {
		SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM_PBKDF2_HMACSHA1);
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, DEFAULT_ITERATIONS, DEFAULT_HASH_WIDTH);
		return factory.generateSecret(spec);
	}

	/**
	 * @param text
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	private static byte[] getMD5(String text) throws NoSuchAlgorithmException {
		return MessageDigest.getInstance(ALGORITHM_MD5).digest(getBytes(text));
	}

	/**
	 * @param bytes
	 * @return
	 * @throws IOException
	 */
	private static byte[] zip(byte[] bytes) throws IOException {
		ByteArrayOutputStream bos = null;
		DeflaterOutputStream dos = null;
		try {
			Deflater deflater = new Deflater();
			deflater.setLevel(6);
			bos = new ByteArrayOutputStream();
			dos = new DeflaterOutputStream(bos, deflater);
			dos.write(bytes);
			dos.close();
			return bos.toByteArray();
		} finally {
			if (bos != null) {
				bos.close();
			}
			if (dos != null) {
				dos.close();
			}
		}
	}

	/**
	 * @param bytes
	 * @return
	 * @throws IOException
	 */
	private static String unzip(byte[] bytes) throws IOException {
		ByteArrayOutputStream bos = null;
		ByteArrayInputStream bis = null;
		InflaterInputStream iis = null;
		try {
			bos = new ByteArrayOutputStream();
			bis = new ByteArrayInputStream(bytes);
			iis = new InflaterInputStream(bis);
			byte[] block = new byte[1024];
			int readLength = iis.read(block);
			while (readLength != -1) {
				bos.write(block, 0, readLength);
				readLength = iis.read(block);
			}
			return new String(bos.toByteArray(), CHARSET_DEFAULT);
		} finally {
			if (iis != null) {
				iis.close();
			}
			if (bis != null) {
				bis.close();
			}
			if (bos != null) {
				bos.close();
			}
		}
	}

	/**
	 * @param text
	 * @return
	 */
	private static byte[] getBytes(String text) {
		if (text == null) {
			return null;
		} else {
			return text.getBytes(CHARSET_DEFAULT);
		}
	}

	/**
	 * @param bytes
	 * @return
	 */
	private static String base64Encode(byte[] bytes) {
		return new BASE64Encoder().encode(bytes);
	}

	/**
	 * @param base64Code
	 * @return
	 * @throws IOException
	 */
	private static byte[] base64Decode(String base64Code) throws IOException {
		return new BASE64Decoder().decodeBuffer(base64Code);
	}

}
