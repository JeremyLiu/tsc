package com.jec.module.business.manage;

import com.jec.module.business.entity.Business;
import com.jec.module.business.entity.Meeting;
import com.jec.module.business.entity.Member;
import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.ProtocolUtils;
import com.jec.utils.Constants;

/**
 * Created by jeremyliu on 5/18/16.
 */
public class MeetManager extends BusinessManager<Meeting> {

    public MeetManager(){
        business = new Business("会议", Constants.DefaultBusinessImg, PduConstants.MONITOR_BUSINESS_MEETING);
    }

    @Override
    protected Meeting pduToEntry(PDU pdu) {

        Meeting target = new Meeting();

        target.setNetunit(ProtocolUtils.getSourId(pdu));

        int offset = PduConstants.LENGTH_OF_HEAD;

        target.setNumber(pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD));
        offset += PduConstants.LENGTH_OF_BCD;

        target.setName(pdu.getString(offset, PduConstants.LENGTH_OF_STR));
        offset += PduConstants.LENGTH_OF_STR;

        target.setState(pdu.getInt8(offset++));

        target.setCaller(pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD));
        offset += PduConstants.LENGTH_OF_BCD;

        int memberCount = pdu.getInt8(offset++);
        for(int i = 0; i < memberCount; i++) {
            Member m = new Member();
            m.setNumber(pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD));
            offset += PduConstants.LENGTH_OF_BCD;
            m.setState(pdu.getInt8(offset++));

            target.getMembers().add(m);
        }

        return target;
    }
}
