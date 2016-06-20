package com.jec.protocol.processor;


import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.ProtocolUtils;

public class ProcessorMeeting implements Processor {

	@Override
	public boolean process(PDU pdu) {
		
		if(PduConstants.CMD_TYPE_YWJS != ProtocolUtils.getCmdType(pdu))
			return false;
		
		if(0x03 != ProtocolUtils.getCmdConfig(pdu))
			return false;

		
//		M_Meeting target = new M_Meeting();
//
//		target.netunit = ProtocolUtils.getSourId(pdu);
//
//		int offset = PduConstants.LENGTH_OF_HEAD;
//
//		target.number = pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD);
//		offset += PduConstants.LENGTH_OF_BCD;
//
//		target.name = pdu.getString(offset, PduConstants.LENGTH_OF_STR);
//		offset += PduConstants.LENGTH_OF_STR;
//
//		target.state = pdu.getInt8(offset++);
//
//		target.caller = pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD);
//		offset += PduConstants.LENGTH_OF_BCD;
//
//		int memberCount = pdu.getInt8(offset++);
//		for(int i = 0; i < memberCount; i++) {
//			Member m = new Member();
//			m.number = pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD);
//			offset += PduConstants.LENGTH_OF_BCD;
//			m.state = pdu.getInt8(offset++);
//
//			target.members.add(m);
//		}
//
//		BusinessMonitor.meeting.setEntry(target);
		
		return true;
	}

}
