package com.jec.protocol.processor;


import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.ProtocolUtils;

public class ProcessorVdr implements Processor {

	@Override
	public boolean process(PDU pdu) {
		
		if(PduConstants.CMD_TYPE_YWJS != ProtocolUtils.getCmdType(pdu))
			return false;
		
		if(0x09 != ProtocolUtils.getCmdConfig(pdu))
			return false;

		
//		M_Vdr target = new M_Vdr();
//
//		target.netunit = ProtocolUtils.getSourId(pdu);
//
//		int offset = PduConstants.LENGTH_OF_HEAD;
//
//		target.vdr = pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD);
//		offset += PduConstants.LENGTH_OF_BCD;
//
//		target.state = pdu.getInt8(offset);
//		offset++;
//
//		target.user.number = pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD);
//		offset += PduConstants.LENGTH_OF_BCD;
//
//		target.user.state = pdu.getInt8(offset);
//		//offset++;
//
//
//		BusinessMonitor.vdr.setEntry(target);
		
		return true;
	}

}
