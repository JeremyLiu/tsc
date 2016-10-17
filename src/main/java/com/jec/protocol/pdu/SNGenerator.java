package com.jec.protocol.pdu;

/**
 * 
 * 此类用于产生命令序号
 * 
 * @author lingdm
 *
 */
public class SNGenerator {
	
	private static int next = 0;
	
	public static synchronized int nextSN() {
		int temp = next;
		next = (next + 1) & 0xFF;
		return temp;
	}

	private SNGenerator() {
	}
	
}
