package com.jec.protocol.pdu.implement;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduBuilder;
import com.jec.protocol.unit.BCD;

public class IncreasedPduBuilder implements PduBuilder {
	
	ByteArrayOutputStream os = new ByteArrayOutputStream(128);

	public IncreasedPduBuilder() {
		super();
	}

	@Override
	public PDU buildPdu() {
		return new DefaultPdu(os.toByteArray());
	}
	
	public int size() {
		return os.size();
	}

	public IncreasedPduBuilder addInteger8(int value) {
		os.write(value);
		return this;
	}
	
	public IncreasedPduBuilder addInteger16(int value) {
		os.write(value >> 8);
		os.write(value);
		return this;
	}
	
	public IncreasedPduBuilder addInteger32(int value) {
		os.write(value >> 24);		
		os.write(value >> 16);
		os.write(value >> 8);
		os.write(value);
		return this;
	}
	
	public IncreasedPduBuilder addString(String value, int length) {
		byte[] buffer = new byte[length]; 
		byte[] data = value.getBytes();
		Arrays.fill(buffer, 0, length, (byte)0);
		System.arraycopy(data, 0, buffer, 0, Math.min(data.length, length));
		os.write(buffer, 0, length);
		return this;
	}
	
	public IncreasedPduBuilder addString(String value) {
		return addString(value, value.length());
	}
	
	public IncreasedPduBuilder addNewLine() {
		return addString("\r\n");
	}
	
	public IncreasedPduBuilder addBCD(BCD value, int length) {
		if(value == null) {
			value = new BCD();
		}
		byte[] data = value.toBytes(length);
		os.write(data, 0, length);
		return this;
	}
	
	public IncreasedPduBuilder addBuilder(IncreasedPduBuilder builder) {
		try {
			os.write(builder.os.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return this;
	}
	
}
