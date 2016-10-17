package com.jec.protocol.pdu;

import java.nio.charset.Charset;

import com.jec.protocol.unit.BCD;

public interface PDU {

	public int getInt8(int offset);
	
	public int getInt16(int offset);
	
	public int getInt32(int offset);
	
	public String getString(int offset, int length);
	
	public String getString(int offset, int length, Charset charset);
	
	public BCD getBCD(int offset, int length);
	
	public int length();
	
	public int offset();
	
	public byte[] buffer();
	
}
