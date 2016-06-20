package com.jec.protocol.processor;


import com.jec.plugin.debug.utils.DebugAgent;
import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.ProtocolUtils;

public class ProcessorPort implements Processor {
	
	DebugAgent debug = new DebugAgent("ProcessorPort");

	@Override
	public boolean process(PDU pdu) {
		
		if(PduConstants.CMD_TYPE_SBJS != ProtocolUtils.getCmdType(pdu))
			return false;
		
		if(0x02 != ProtocolUtils.getCmdConfig(pdu))
			return false;
		
		if(pdu.length() != (PduConstants.LENGTH_OF_HEAD + 4))
			return false;
		
		int offset = PduConstants.LENGTH_OF_HEAD;
		
		int netunit = ProtocolUtils.getSourId(pdu);
		offset++;//int type = pdu.getInt8(offset++);
		int slot = pdu.getInt8(offset);
		offset++;
		int port = pdu.getInt8(offset);
		offset++;
		int state = pdu.getInt8(offset);
		//offset++;
		
		debug.message("????????, netunit=" + netunit + " slot=" + slot + " port=" + port + " state=" + state);
		
		
//		SdhStateManager.Default.setPortState(netunit, slot, port, state);
		
		return true;
	}

}
