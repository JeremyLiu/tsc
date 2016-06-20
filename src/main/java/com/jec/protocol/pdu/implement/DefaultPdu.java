package com.jec.protocol.pdu.implement;

import java.util.Arrays;

import com.jec.protocol.unit.BCD;
import com.jec.utils.lang.HexUtils;


public class DefaultPdu extends BufferedPdu {
	
	public DefaultPdu(byte[] buffer) {
		super();
		this.setData(buffer, 0, buffer.length);
	}


	public void setInt8(int offset, int value) {
		buffer[ofs(offset)] = (byte)value;
	}

	public void setInt16(int offset, int value) {
		buffer[ofs(offset) + 0] = (byte)(value >> 8);
		buffer[ofs(offset) + 1] = (byte)(value);
	}

	public void setInt32(int offset, int value) {
		buffer[ofs(offset) + 0] = (byte)(value >> 24);
		buffer[ofs(offset) + 1] = (byte)(value >> 16);
		buffer[ofs(offset) + 2] = (byte)(value >> 8);
		buffer[ofs(offset) + 3] = (byte)(value);
	}

	public void setString(int offset, int length, String value) {
		byte[] data = value.getBytes();
		Arrays.fill(buffer, ofs(offset), ofs(offset) + length, (byte)0);
		System.arraycopy(data, 0, buffer, ofs(offset), Math.min(data.length, length));
	}

	public void setBCD(int offset, int length, BCD value) {
		
		byte[] data = value.toBytes(length);
		System.arraycopy(data, 0, buffer, ofs(offset), length);
		
	}

	@Override
	public String toString() {
		return "PDU size=" + buffer.length + " data=["  + HexUtils.toHex(buffer, Math.min(16, buffer.length)) + "]";
	}
	
	
	
	
}
