package com.jec.module.business.manage;

import com.jec.module.business.entity.Business;
import com.jec.module.business.entity.Threetalk;
import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.ProtocolUtils;
import com.jec.utils.Constants;

/**
 * Created by jeremyliu on 5/18/16.
 */
public class ThreeTalkManager extends BusinessManager<Threetalk> {

    public ThreeTalkManager(){
        business = new Business("三方通话", Constants.DefaultBusinessImg, PduConstants.MONITOR_BUSINESS_THREETALK);
    }

    @Override
    protected Threetalk pduToEntry(PDU pdu) {
        Threetalk target = new Threetalk();

        target.setNetunit(ProtocolUtils.getSourId(pdu));

        int offset = PduConstants.LENGTH_OF_HEAD;

        target.setCaller(pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD));
        offset += PduConstants.LENGTH_OF_BCD;

        target.setState(pdu.getInt8(offset));
        offset++;

        target.getSecond().setNumber(pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD));
        offset += PduConstants.LENGTH_OF_BCD;

        target.getSecond().setState(pdu.getInt8(offset));
        offset++;

        target.getThird().setNumber(pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD));
        offset += PduConstants.LENGTH_OF_BCD;

        target.getThird().setState(pdu.getInt8(offset));
        return target;
    }
}
