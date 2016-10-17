package com.jec.protocol.processor;


import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.ProtocolUtils;

public class ProcessorThreetalk implements Processor {

	@Override
	public boolean process(PDU pdu) {
		
		if(PduConstants.CMD_TYPE_YWJS != ProtocolUtils.getCmdType(pdu))
			return false;
		
		if(0x04 != ProtocolUtils.getCmdConfig(pdu))
			return false;

		
//		M_Threetalk target = new M_Threetalk();
//
//		target.netunit = ProtocolUtils.getSourId(pdu);
//
//		int offset = PduConstants.LENGTH_OF_HEAD;
//
//		target.caller = pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD);
//		offset += PduConstants.LENGTH_OF_BCD;
//
//		target.state = pdu.getInt8(offset);
//		offset++;
//
//		target.second.number = pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD);
//		offset += PduConstants.LENGTH_OF_BCD;
//
//		target.second.state = pdu.getInt8(offset);
//		offset++;
//
//		target.third.number = pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD);
//		offset += PduConstants.LENGTH_OF_BCD;
//
//		target.third.state = pdu.getInt8(offset);
//		//offset++;
//
//
//		BusinessMonitor.threetalk.setEntry(target);
		
		return true;
	}

}
