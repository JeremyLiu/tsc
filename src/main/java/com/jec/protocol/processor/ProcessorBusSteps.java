package com.jec.protocol.processor;

import com.jec.plugin.debug.utils.DebugAgent;
import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.ProtocolUtils;
//import com.jec.protocol.unit.BCD;

public class ProcessorBusSteps implements Processor {

	DebugAgent debug = null;

//	static class BusStepImpl implements BusSteper.BusStep {
//
//		BCD number = null;
//
//		int event = 0;
//
//		String text = "";
//
//		public BusStepImpl(BCD number, int event, String text) {
//			super();
//			this.number = number;
//			this.event = event;
//			this.text = text;
//		}
//
//
//		@Override
//		public String getBusNumber() {
//			return number.toString();
//		}
//
//		@Override
//		public int getEvent() {
//			return event;
//		}
//
//		@Override
//		public String getText() {
//			return text;
//		}
//
//	}



	public ProcessorBusSteps() {
		super();

		debug = new DebugAgent("监视.业务进展");
	}



	@Override
	public boolean process(PDU pdu) {

		if(PduConstants.CMD_TYPE_YWJS != ProtocolUtils.getCmdType(pdu))
			return false;

		if(0x0E != ProtocolUtils.getCmdConfig(pdu))
			return false;


		//target.netunit = ProtocolUtils.getSourId(pdu);

//		int offset = PduConstants.LENGTH_OF_HEAD;
//
//		BCD number = pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD);
//		offset += PduConstants.LENGTH_OF_BCD;
//
//		int event = pdu.getInt16(offset);
//		offset += 2;
//
//		int textLen = pdu.getInt8(offset);
//		offset += 1;
//
//		String text = pdu.getString(offset, textLen);
		//offset += textLen;






//		debug.debug("number=" + number.toString() + " event=" + event + " text=" + text);
//		System.out.println("number=" + number.toString() + " event=" + event + " text=" + text);
//
//		BusinessStepsMonitor.fireStep(new BusStepImpl(number, event, text));

		return true;
	}

}

