package com.jec.utils.lang;

public class ShortHelper {


	private ShortHelper() {
	}
	
	public static byte getByte1(short source) {
		return (byte)(source & 0xFF);
	}
	
	public static byte getByte2(short source) {
		return (byte)((source & 0xFF00)>> 8);
	}
	
	public static short build(byte b1, byte b2) {
		return (short)((b1 & 0xFF) | ((b2 & 0xFF) << 8));
	}

	
}
