package com.jec.protocol.processor;

import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.ProtocolUtils;

public class ProcessorBroadcast implements Processor {

	@Override
	public boolean process(PDU pdu) {
		
		if(PduConstants.CMD_TYPE_YWJS != ProtocolUtils.getCmdType(pdu))
			return false;
		
		if(0x07 != ProtocolUtils.getCmdConfig(pdu))
			return false;

		
//		M_Broadcast target = new M_Broadcast();
//
//		target.netunit = ProtocolUtils.getSourId(pdu);
//
//		int offset = PduConstants.LENGTH_OF_HEAD;
//
//		target.number = pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD);
//		offset += PduConstants.LENGTH_OF_BCD;
//
//		target.device = pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD);
//		offset += PduConstants.LENGTH_OF_BCD;
//
//		target.name = pdu.getString(offset, PduConstants.LENGTH_OF_STR);
//		offset += PduConstants.LENGTH_OF_STR;
//
//		target.state = pdu.getInt8(offset++);
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
//		BusinessMonitor.broadcast.setEntry(target);
		
		return true;
	}

}
