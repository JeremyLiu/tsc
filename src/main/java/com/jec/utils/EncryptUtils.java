package com.jec.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

public class EncryptUtils {
	public static String encryptPlainPassword(String password) {
		return encryptMd5Password(DigestUtils.md5Hex(password));
	}

	public static String encryptMd5Password(String password) {
		if (StringUtils.length(password) != 32) {
			throw new IllegalArgumentException("MD5 string required!");
		}

		String salt = StringUtils.substring(password, 27);
		return DigestUtils.md5Hex(password + salt);
	}

}
