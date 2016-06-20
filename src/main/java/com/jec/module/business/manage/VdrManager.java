package com.jec.module.business.manage;

import com.jec.module.business.entity.Business;
import com.jec.module.business.entity.Member;
import com.jec.module.business.entity.Vdr;
import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.ProtocolUtils;
import com.jec.utils.Constants;

/**
 * Created by jeremyliu on 6/18/16.
 */
public class VdrManager extends BusinessManager<Vdr> {

    public VdrManager(){
        business = new Business("VDR录音", Constants.DefaultBusinessImg, PduConstants.MONITOR_BUSINESS_VDRRECORD);
    }

    @Override
    protected Vdr pduToEntry(PDU pdu) {
        Vdr target = new Vdr();
        target.setNetunit(ProtocolUtils.getSourId(pdu));

        int offset = PduConstants.LENGTH_OF_HEAD;

        target.setVdr(pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD));
        offset += PduConstants.LENGTH_OF_BCD;

        target.setState(pdu.getInt8(offset));
        offset++;

        Member member = new Member();
        member.setNumber(pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD));
        offset += PduConstants.LENGTH_OF_BCD;

        member.setState(pdu.getInt8(offset));

        target.setUser(member);

        return target;
    }
}
