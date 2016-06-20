package com.jec.protocol.processor;

import com.jec.plugin.debug.utils.DebugAgent;
import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.ProtocolUtils;

public class ProcessorCard implements Processor {
	
	DebugAgent debug = new DebugAgent("ProcessorCard");

	@Override
	public boolean process(PDU pdu) {
		
		if(PduConstants.CMD_TYPE_SBJS != ProtocolUtils.getCmdType(pdu))
			return false;
		
		if(0x01 != ProtocolUtils.getCmdConfig(pdu))
			return false;
		
		if(pdu.length() != (PduConstants.LENGTH_OF_HEAD + 3))
			return false;
		
		int offset = PduConstants.LENGTH_OF_HEAD;
		
		int netunit = ProtocolUtils.getSourId(pdu);
		offset++;//int type = pdu.getInt8(offset++);
		int slot = pdu.getInt8(offset);
		offset++;
		int state = pdu.getInt8(offset);
		//offset++;
		
		debug.message("板卡状态上报, netunit=" + netunit + " slot=" + slot + " state=" + state);
		
//		SdhStateManager.Default.setCardState(netunit, slot, state);
		
		return true;
	}

}
