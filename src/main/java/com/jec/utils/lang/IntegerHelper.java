package com.jec.utils.lang;

public class IntegerHelper {

	private IntegerHelper() {
	}
	
	public static byte getByte1(int source) {
		return (byte)(source & 0xFF);
	}
	
	public static byte getByte2(int source) {
		return (byte)((source & 0xFF00)>> 8);
	}
	
	public static byte getByte3(int source) {
		return (byte)((source & 0xFF0000)>> 16);
	}
	
	public static byte getByte4(int source) {
		return (byte)(source >> 24);
	}
	
	public static int build(byte b1, byte b2, byte b3, byte b4) {
		return (b1 & 0xFF) | ((b2 & 0xFF) << 8) | ((b3 & 0xFF) << 16) | ((b4 & 0xFF) << 24);
	}
	
	public static int build(int hight16, int low16) {
		return ((hight16 & 0xFFFF) << 16) + (low16 & 0xFFFF);
	}
	
	public static int getHight16(int source) {
		return (source >> 16) & 0xFFFF;
	}
	
	public static int getLow16(int source) {
		return source & 0xFFFF;
	}

}
