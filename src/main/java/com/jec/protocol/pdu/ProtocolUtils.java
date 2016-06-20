package com.jec.protocol.pdu;

public class ProtocolUtils {
	
	public static int getJump(PDU pdu) {
		return pdu.getInt8(0);
	}
	
	public static int getDestId(PDU pdu) {
		return pdu.getInt8(1);
	}
	
	public static int getSourId(PDU pdu) {
		return pdu.getInt8(2);
	}
	
	public static int getProtocolType(PDU pdu) {
		return pdu.getInt8(3);
	}
	
	public static int getBodySize(PDU pdu) {
		return pdu.getInt16(4);
	}
	
	public static int getCmdSN(PDU pdu) {
		return pdu.getInt8(6);
	}
	
	public static int getCmdType(PDU pdu) {
		return pdu.getInt8(7);
	}
	
	public static int getCmdCode(PDU pdu) {
		return pdu.getInt8(8);
	}	
	
	public static int getCmdConfig(PDU pdu) {
		return pdu.getInt8(9);
	}	
	
	public static int getCardType(PDU pdu) {
		return pdu.getInt8(10);
	}	
	
	public static int getCardSlot(PDU pdu) {
		return pdu.getInt8(11);
	}		

}
