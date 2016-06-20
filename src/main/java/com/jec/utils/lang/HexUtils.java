package com.jec.utils.lang;

public class HexUtils {

	public static String toHex(byte value) {
		int v = value & 0xFF;
		String hex = Integer.toHexString(v);
		if(hex.length() == 1) {
			hex = "0" + hex;
		}
		return hex;
	}
	
	public static String toHex(byte[] value, int length, int lineWidth) {
		
		
		StringBuilder sb = new StringBuilder();
		
		int lineCount = 0;
		int count = 0;
		
		for(byte v : value) {
			sb.append(toHex(v));
			sb.append(" ");
			lineCount++;
			if(lineCount >= lineWidth) {
				sb.append("\r\n");
				lineCount = 0;
			}
			count++;
			if(count >= length) {
				break;
			}
		}
		
		return sb.toString();
		
	}
	
	public static String toHex(byte[] value, int length) {
		
		return toHex(value, length, Integer.MAX_VALUE);
		
	}	
	
}
