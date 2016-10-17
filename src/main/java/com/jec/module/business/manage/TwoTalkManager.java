package com.jec.module.business.manage;

import com.jec.module.business.entity.Business;
import com.jec.module.business.entity.Twotalk;
import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.ProtocolUtils;
import com.jec.utils.Constants;

/**
 * Created by jeremyliu on 5/19/16.
 */
public class TwoTalkManager extends BusinessManager<Twotalk> {

    public TwoTalkManager(){
        business = new Business("点对点", Constants.DefaultBusinessImg, PduConstants.MONITOR_BUSINESS_P2PTALK);
    }

    @Override
    protected Twotalk pduToEntry(PDU pdu) {
        Twotalk target = new Twotalk();
        target.setNetunit(ProtocolUtils.getSourId(pdu));

        int offset = PduConstants.LENGTH_OF_HEAD;

        target.setCaller(pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD));
        offset += PduConstants.LENGTH_OF_BCD;

        target.setState(pdu.getInt8(offset++));

        target.getSecond().setNumber(pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD));
        offset += PduConstants.LENGTH_OF_BCD;

        target.getSecond().setState(pdu.getInt8(offset++));
        return  target;
    }
}
