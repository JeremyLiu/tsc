package com.jec.protocol.processor;


import com.jec.plugin.debug.utils.DebugAgent;
import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.ProtocolUtils;

public class ProcessorTongling implements Processor {
	
	DebugAgent debug = null;

	public ProcessorTongling() {
		super();
		debug = new DebugAgent("ProcessorTongling");
	}

	@Override
	public boolean process(PDU pdu) {
		
		if(PduConstants.CMD_TYPE_YWJS != ProtocolUtils.getCmdType(pdu))
			return false;
		
		if(0x02 != ProtocolUtils.getCmdConfig(pdu))
			return false;

		
//		M_Tongling target = new M_Tongling();
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
//		target.state = pdu.getInt8(offset);
//		offset++;
//
//		target.chairman = pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD);
//		offset += PduConstants.LENGTH_OF_BCD;
//
//		target.nested = pdu.getInt8(offset);
//		offset++;
//
//		target.superior = pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD);
//		offset += PduConstants.LENGTH_OF_BCD;
//
//		int commanderCount = pdu.getInt8(offset++);
//		for(int i = 0; i < commanderCount; i++) {
//			Member m = new Member();
//			m.number = pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD);
//			offset += PduConstants.LENGTH_OF_BCD;
//			m.state = pdu.getInt8(offset);
//			offset++;
//
//			target.commanders.add(m);
//		}
//
//		int memberCount = pdu.getInt8(offset++);
//		for(int i = 0; i < memberCount; i++) {
//			Member m = new Member();
//			m.number = pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD);
//			offset += PduConstants.LENGTH_OF_BCD;
//			m.state = pdu.getInt8(offset);
//			offset++;
//
//			target.members.add(m);
//		}
//
//		debug.debug("ͨ�������ϱ�    ���룺" + target.number.toString() + "  ״̬��" + target.state);
//
//		BusinessMonitor.tongling.setEntry(target);
		
		return true;
	}

}
