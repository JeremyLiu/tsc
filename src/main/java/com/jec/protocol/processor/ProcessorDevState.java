package com.jec.protocol.processor;

import com.jec.plugin.debug.utils.DebugAgent;
import com.jec.plugin.framework.Framework;
import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.ProtocolUtils;
import com.jec.protocol.unit.BCD;
//import com.jec.protocol.unit.BCD;

public class ProcessorDevState implements Processor {
	
//	DevService dev = null;
	
	DebugAgent debug = null;

//	public ProcessorDevState() {
//		super();
//
//		dev = (DevService) Framework.getService(DevService.class);
//		if(dev == null) {
//			throw new RuntimeException("DevService not found.");
//		}
//
//		debug = new DebugAgent("MonitorDevState");
//	}
	
	

	@Override
	public boolean process(PDU pdu) {

		
		if(0x04 != ProtocolUtils.getCmdConfig(pdu))
			return false;

		int offset = PduConstants.LENGTH_OF_HEAD;
		
		BCD number = pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD);
		offset += PduConstants.LENGTH_OF_BCD;

		int stateCode = pdu.getInt8(offset);
//		//offset++;
//		DeviceState state = DeviceState.fromInteger(stateCode);
//
//		DevicePort port = dev.byUport(number);
//		if(port == null) {
//			debug.warn("Code[" + number.toString() + "] not map to a DevicePort");
//			return true;
//		}
//
//		Device device = port.getDevice();
//
//		//debug.debug("Device state change, device=" + device.getName() + " state=" + state);
//		DeviceStateManager.DEFAULT.setState(device, state);

		return true;
	}

}

