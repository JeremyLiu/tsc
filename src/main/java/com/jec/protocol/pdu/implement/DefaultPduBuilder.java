package com.jec.protocol.pdu.implement;

import java.util.Arrays;

import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduBuilder;

public class DefaultPduBuilder extends DefaultPdu implements PduBuilder {

	public DefaultPduBuilder(int size) {
		super(new byte[size]);
	}	
	
	public DefaultPduBuilder(byte[] data) {
		super(data);
	}
	
	public DefaultPduBuilder(byte[] data, int offset, int length) {
		super(Arrays.copyOfRange(data, offset, offset + length));
	}

	@Override
	public PDU buildPdu() {
		return this;
	}
	
}
