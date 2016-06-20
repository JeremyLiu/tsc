package com.jec.utils.tool;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Encryption implements Encrypt{

	@Override
	public String getCrypticCode(String code) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] resultBytes = md.digest(code.getBytes());
			String resultStr = byteArrayToHexString(resultBytes);
			return resultStr;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	private final static String[] hexDigits = { 
		"0", "1", "2", "3", "4", "5", "6", "7", 
		"8", "9", "a", "b", "c", "d", "e", "f"}; 
	/**
	 * 转换字节数组为16进制字串 
	 * @param b 字节数组 
	 * @return 16进制字串 
	 */ 
	private static String byteArrayToHexString(byte[] b) { 
		StringBuffer resultSb = new StringBuffer(); 
		for (int i = 0; i < b.length; i++) { 
			resultSb.append(byteToHexString(b[i])); 
		} 
		return resultSb.toString(); 
	} 

	private static String byteToHexString(byte b) { 
		int n = b; 
		if (n < 0) 
			n = 256 + n; 
		int d1 = n / 16; 
		int d2 = n % 16; 
		return hexDigits[d1] + hexDigits[d2]; 
	} 

}
