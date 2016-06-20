package com.jec.protocol.unit;

import com.jec.protocol.unit.BCD;

import java.util.Arrays;

public class BytesWrap {

	byte[] buffer = null;
	int off = 0;
	int len = 0;
	
	public BytesWrap() {
	}

	public BytesWrap(byte[] buffer) {
		this(buffer, 0, buffer.length);
	}
	
	public BytesWrap(byte[] buffer, int off, int len) {
		this.buffer = buffer;
		this.off = off;
		this.len = len;
	}
	
	public void setData(byte[] buffer, int off, int len) {
		this.buffer = buffer;
		this.off = off;
		this.len = len;
	}
	
	
	
	public byte[] buffer() {
		return buffer;
	}

	public int offset() {
		return off;
	}
	
	public int length() {
		return this.len;
	}
	
	int intAt(int offset) {
		return buffer[offset + off] & 0xFF;
	}

	public int getInt8(int offset) {
		if(!validate(offset, 1)) {
			throw new ArrayIndexOutOfBoundsException();
		}

		return intAt(offset);
	}

	
	public int getInt16(int offset) {
		
		if(!validate(offset, 2)) {
			throw new ArrayIndexOutOfBoundsException();
		}
		
		int v = ((intAt(offset) << 8) + intAt(offset + 1));

		return v;
	}



	public int getInt32(int offset) {
		
		if(!validate(offset, 4)) {
			throw new ArrayIndexOutOfBoundsException();
		}
		
		int v = (intAt(offset) << 24) + 
				(intAt(offset + 1) << 16) +
				(intAt(offset + 2) << 8) + 
				intAt(offset + 3);
		
		return v;
	}
	
	
	public String getString(int offset, int length) {

		if(!validate(offset, length)) {
			throw new ArrayIndexOutOfBoundsException();
		}

		String v = new String(buffer, offset + off, length);

		return v;	
	}
	
	

	
	public BCD getBCD(int offset, int length) {

		if(!validate(offset, length)) {
			throw new ArrayIndexOutOfBoundsException();
		}
		
		BCD v = BCD.fromBytes(buffer, offset + off, length);
		if(v == null) {
			throw new RuntimeException("���ݸ�ʽ�����޷���BCD�����");
		}

		return v;	
	}
	
	public byte[] toBytes() {
		return Arrays.copyOfRange(buffer, off, off + len);
	}
	
	public byte[] toBytes(int offset, int length) {
		return Arrays.copyOfRange(buffer, off + offset, off + offset + length);
	}

	boolean validate(int offset, int length) {
		if(buffer == null)
			return false;
		return (this.off + this.len) >= (offset + off + length);
	}

	public void setInt8(int offset, int value) {
		buffer[offset + off] = (byte)value;
	}

	public void setInt16(int offset, int value) {
		buffer[offset + off + 0] = (byte)(value >> 8);
		buffer[offset + off + 1] = (byte)(value);
	}

	public void setInt32(int offset, int value) {
		buffer[offset + off + 0] = (byte)(value >> 24);
		buffer[offset + off + 1] = (byte)(value >> 16);
		buffer[offset + off + 2] = (byte)(value >> 8);
		buffer[offset + off + 3] = (byte)(value);
	}

	public void setString(int offset, int length, String value) {
		byte[] data = value.getBytes();
		Arrays.fill(buffer, offset, offset + off + length, (byte)0);
		System.arraycopy(data, 0, buffer, offset + off, Math.min(data.length, length));
	}

	public void setBCD(int offset, int length, BCD value) {
		
		byte[] data = value.toBytes(length);
		System.arraycopy(data, 0, buffer, offset + off, length);
		
	}
	
}
