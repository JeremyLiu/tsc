package com.jec.protocol.pdu.implement;

import java.nio.charset.Charset;

import com.jec.protocol.pdu.PDU;
import com.jec.protocol.unit.BCD;
import com.jec.utils.lang.HexUtils;

public class BufferedPdu implements PDU {

	byte[] buffer;
	
	int off;
	
	int len;

	public BufferedPdu() {
	}
	
	public void setData(byte[] buffer, int off, int len) {
		this.buffer = buffer;
		this.off = off;
		this.len = len;
	}
	
	/**
	 * 计算实际偏移量
	 */
	protected int ofs(int offset) {
		return offset + off;
	}
	
	protected int intAt(int offset) {
		return (buffer[ofs(offset)] & 0xFF);
	}
	
	@Override
	public int getInt8(int offset) {
		if(!validate(ofs(offset), 1)) {
			exception("读取数据越界");
		}

		return intAt(ofs(offset));
	}

	@Override
	public int getInt16(int offset) {
		
		if(!validate(ofs(offset), 2)) {
			exception("读取数据越界");
		}
		
		int v = (intAt(ofs(offset)) << 8) + intAt(ofs(offset) + 1);

		return v;
	}



	public int getInt32(int offset) {
		
		if(!validate(ofs(offset), 4)) {
			exception("读取数据越界");
		}
		
		int v = (intAt(ofs(offset)) << 24) + 
				(intAt(ofs(offset) + 1) << 16) +
				(intAt(ofs(offset) + 2) << 8) + 
				intAt(ofs(offset) + 3);
		
		return v;
	}
	
	@Override
	public String getString(int offset, int length) {

		if(!validate(ofs(offset), length)) {
			exception("读取数据越界");
		}
		
		for(int i = 0; i < length; i++) {
			if(buffer[ofs(offset) + i] == 0) {
				length = i;
				break;
			}
		}

		String v = new String(buffer, ofs(offset), length);

		return v.trim();	
	}

	@Override
	public String getString(int offset, int length, Charset charset) {
		
		if(!validate(ofs(offset), length)) {
			exception("读取数据越界");
		}
		
		for(int i = 0; i < length; i++) {
			if(buffer[ofs(offset) + i] == 0) {
				length = i;
				break;
			}
		}

		String v = new String(buffer, ofs(offset), length, charset);

		return v.trim();	
	}

	@Override
	public BCD getBCD(int offset, int length) {

		if(!validate(ofs(offset), length)) {
			exception("读取数据越界");
		}
		
		BCD v = BCD.fromBytes(buffer, ofs(offset), length);
		if(v == null) {
			exception("数据格式错误，无法用BCD码解析");
		}

		return v;	
	}

	@Override
	public int length() {
		return len;
	}
	
	

	@Override
	public int offset() {
		// TODO Auto-generated method stub
		return off;
	}

	@Override
	public byte[] buffer() {
		// TODO Auto-generated method stub
		return buffer;
	}

	protected boolean validate(int offset, int length) {
		if(buffer == null)
			return false;
		return buffer.length >= (ofs(offset) + length);
	}

	protected void exception(String message) {
		throw new RuntimeException(this.getClass().getSimpleName() + ": " + message);
	}

	@Override
	public String toString() {
		return "PDU size=" + buffer.length + " data=["  + HexUtils.toHex(buffer, Math.min(16, buffer.length)) + "]";
	}
	
	
	
}
