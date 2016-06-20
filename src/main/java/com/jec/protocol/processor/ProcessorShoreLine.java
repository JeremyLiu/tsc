package com.jec.protocol.processor;


import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.ProtocolUtils;

public class ProcessorShoreLine implements Processor {

	@Override
	public boolean process(PDU pdu) {
		
		if(PduConstants.CMD_TYPE_YWJS != ProtocolUtils.getCmdType(pdu))
			return false;
		
		int config = ProtocolUtils.getCmdConfig(pdu);
		if(config != 0x05 && config != 0x06 )
			return false;

		
//		M_ShoreLine target = new M_ShoreLine();
//		target.type = (config == 0x05) ? M_ShoreLine.TYPE_SR : M_ShoreLine.TYPE_DR;
//
//		target.netunit = ProtocolUtils.getSourId(pdu);
//
//		int offset = PduConstants.LENGTH_OF_HEAD;
//
//		target.slot = pdu.getInt8(offset);
//		offset++;
//
//		target.ts = pdu.getInt8(offset);
//		offset++;
//
//
//		target.shoreNumber = pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD);
//		offset += PduConstants.LENGTH_OF_BCD;
//
//		target.shoreState = pdu.getInt8(offset);
//		offset++;
//
//		target.number = pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD);
//		offset += PduConstants.LENGTH_OF_BCD;
//
//		target.state = pdu.getInt8(offset);
//		//offset++;
//
//		BusinessMonitor.shoreLine.setEntry(target);
		
		return true;
	}

}

