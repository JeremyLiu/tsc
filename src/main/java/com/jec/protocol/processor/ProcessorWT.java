package com.jec.protocol.processor;

import com.jec.plugin.debug.utils.DebugAgent;
import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.ProtocolUtils;

public class ProcessorWT implements Processor {
	
	DebugAgent debug = null;
	
	public ProcessorWT() {
		super();
		// TODO Auto-generated constructor stub
		debug = new DebugAgent("MonitorWT");
	}

	@Override
	public boolean process(PDU pdu) {
		
		if(PduConstants.CMD_TYPE_YWJS != ProtocolUtils.getCmdType(pdu))
			return false;
		
		if(0x0B != ProtocolUtils.getCmdConfig(pdu))
			return false;

		
//		M_WtBusiness target = new M_WtBusiness();
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
//		target.type = pdu.getInt8(offset++);
//
//		target.state = pdu.getInt8(offset++);
//
//		target.chairman = pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD);
//		offset += PduConstants.LENGTH_OF_BCD;
//
//		int listenerCount = pdu.getInt8(offset++);
//		for(int i = 0; i < listenerCount; i++) {
//			BCD listener = pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD);
//			offset += PduConstants.LENGTH_OF_BCD;
//
//			target.listeners.add(listener);
//		}
//
//		int deviceCount = pdu.getInt8(offset++);
//		for(int i = 0; i < deviceCount; i++) {
//			BCD device = pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD);
//			offset += PduConstants.LENGTH_OF_BCD;
//
//			target.devices.add(device);
//		}
//
//		debug.debug("number:" + target.number.toString() + " state:" + target.state);
//
//		BusinessMonitor.wt.setEntry(target);
		
		return true;
	}

}

