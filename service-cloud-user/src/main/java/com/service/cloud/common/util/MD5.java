package com.service.cloud.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

/**
 * Md5摘要信息算法
 * 
 */
public class MD5 {
	/**
	 * Used to build output as Hex
	 */
	private static final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 获取输入字符串的MD5
	 * 
	 * @param input
	 * @return
	 */
	public static String md5(String input) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(input.getBytes("UTF-8"));
			return encodeHex(messageDigest);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取文件的MD5 参数 文件
	 * 
	 * @param file
	 * @return
	 */
	public static String md5(File file) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			FileInputStream fis = new FileInputStream(file);
			byte[] buffer = new byte[1024];
			int numRead = 0;
			while ((numRead = fis.read(buffer)) > 0) {
				md.update(buffer, 0, numRead);
			}
			fis.close();
			byte[] messageDigest = md.digest();
			return encodeHex(messageDigest).toUpperCase();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 对byte数组进行md5计算
	 * 
	 * @param bytes
	 * @return
	 */
	public static String md5(byte[] bytes) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(bytes);
			return encodeHex(messageDigest);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Converts an array of bytes into an array of characters representing the
	 * hexadecimal values of each byte in order
	 * 
	 * @param messageDigest
	 * @return
	 */
	protected static String encodeHex(final byte[] messageDigest) {
		final int l = messageDigest.length;
		final char[] out = new char[l << 1];
		// two characters form the hex value.
		for (int i = 0, j = 0; i < l; i++) {
			out[j++] = DIGITS_LOWER[(0xF0 & messageDigest[i]) >>> 4];
			out[j++] = DIGITS_LOWER[0x0F & messageDigest[i]];
		}
		return new String(out);
	}
}
