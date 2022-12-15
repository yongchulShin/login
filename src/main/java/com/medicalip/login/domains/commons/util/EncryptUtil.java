package com.medicalip.login.domains.commons.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

/**
 * 암호화 유틸리티 클래스.
 * 
 */
public abstract class EncryptUtil {

	/**
	 * MD5.
	 * 
	 * @param input
	 *            입력 문자열
	 * @return 암호화된 문자열
	 * @throws NoSuchAlgorithmException
	 */
	public static String md5(String input) throws NoSuchAlgorithmException {
		return encrypt(input, "MD5");
	}

	/**
	 * SHA-1.
	 * 
	 * @param input
	 *            입력
	 * @return 암호화된 문자열
	 * @throws NoSuchAlgorithmException
	 */
	public static String sha1(String input) throws NoSuchAlgorithmException {
		return encrypt(input, "SHA-1");
	}

	/**
	 * SHA-256.
	 * 
	 * @param input
	 *            입력
	 * @return 암호화된 문자열
	 * @throws NoSuchAlgorithmException
	 */
	public static String sha256(String input) throws NoSuchAlgorithmException {
		return encrypt(input, "SHA-256");
	}

	/**
	 * SHA-384.
	 * 
	 * @param input
	 *            입력
	 * @return 암호화된 문자열
	 * @throws NoSuchAlgorithmException
	 */
	public static String sha384(String input) throws NoSuchAlgorithmException {
		return encrypt(input, "SHA-384");
	}

	/**
	 * SHA-512.
	 * 
	 * @param input
	 *            입력
	 * @return 암호화된 문자열
	 * @throws NoSuchAlgorithmException
	 */
	public static String sha512(String input) throws NoSuchAlgorithmException {
		return encrypt(input, "SHA-512");
	}

	/**
	 * 암호화.
	 * 
	 * @param input
	 *            입력 문자열
	 * @param algorithm
	 *            알고리즘
	 * @return 암호화된 문자열
	 * @throws NoSuchAlgorithmException
	 */
	public static String encrypt(String input, String algorithm) throws NoSuchAlgorithmException {
		if (input != null && !input.isEmpty()) {
			MessageDigest md = MessageDigest.getInstance(algorithm);
			md.update(input.getBytes());
			input = new String(Hex.encodeHex(md.digest()));
		}
		return input;
	}

}
