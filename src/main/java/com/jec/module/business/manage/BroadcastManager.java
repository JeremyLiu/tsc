package com.jec.module.business.manage;

import com.jec.module.business.entity.Broadcast;
import com.jec.module.business.entity.Business;
import com.jec.module.business.entity.Member;
import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.ProtocolUtils;
import com.jec.utils.Constants;

/**
 * Created by jeremyliu on 5/18/16.
 */
public class BroadcastManager extends BusinessManager<Broadcast> {

    public BroadcastManager(){
        this.business = new Business("文艺广播", Constants.DefaultBusinessImg,PduConstants.MONITOR_BUSINESS_BROADCAST);
    }

    @Override
    protected Broadcast pduToEntry(PDU pdu) {
        Broadcast target = new Broadcast();

        target.setNetunit(ProtocolUtils.getSourId(pdu));

        int offset = PduConstants.LENGTH_OF_HEAD;

        target.setNumber(pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD));
        offset += PduConstants.LENGTH_OF_BCD;

        target.setDevice(pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD));
        offset += PduConstants.LENGTH_OF_BCD;

        target.setName(pdu.getString(offset, PduConstants.LENGTH_OF_STR));
        offset += PduConstants.LENGTH_OF_STR;

        target.setState(pdu.getInt8(offset++));

        int memberCount = pdu.getInt8(offset++);
        for(int i = 0; i < memberCount; i++) {
            Member m = new Member();
            m.setNumber(pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD));
            offset += PduConstants.LENGTH_OF_BCD;
            m.setState(pdu.getInt8(offset++));

            target.members.add(m);
        }
        return target;
    }
}
