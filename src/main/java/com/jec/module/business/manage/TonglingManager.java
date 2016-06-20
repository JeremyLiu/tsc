package com.jec.module.business.manage;

import com.jec.module.business.entity.Business;
import com.jec.module.business.entity.Member;
import com.jec.module.business.entity.Tongling;
import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.ProtocolUtils;
import com.jec.utils.Constants;

/**
 * Created by jeremyliu on 5/19/16.
 */
public class TonglingManager extends BusinessManager<Tongling> {

    public TonglingManager(){
        business = new Business("通令", Constants.DefaultBusinessImg, PduConstants.MONITOR_BUSINESS_TONGLING);
    }

    @Override
    protected Tongling pduToEntry(PDU pdu) {
        Tongling target = new Tongling();

        target.setNetunit(ProtocolUtils.getSourId(pdu));

        int offset = PduConstants.LENGTH_OF_HEAD;

        target.setNumber(pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD));
        offset += PduConstants.LENGTH_OF_BCD;

        target.setName(pdu.getString(offset, PduConstants.LENGTH_OF_STR));
        offset += PduConstants.LENGTH_OF_STR;

        target.setState(pdu.getInt8(offset));
        offset++;

        target.setChairman(pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD));
        offset += PduConstants.LENGTH_OF_BCD;

        target.setNested(pdu.getInt8(offset));
        offset++;

        target.setSuperior(pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD));
        offset += PduConstants.LENGTH_OF_BCD;

        int commanderCount = pdu.getInt8(offset++);
        for(int i = 0; i < commanderCount; i++) {
            Member m = new Member();
            m.setNumber(pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD));
            offset += PduConstants.LENGTH_OF_BCD;
            m.setState(pdu.getInt8(offset));
            offset++;

            target.getCommanders().add(m);
        }

        int memberCount = pdu.getInt8(offset++);
        for(int i = 0; i < memberCount; i++) {
            Member m = new Member();
            m.setNumber(pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD));
            offset += PduConstants.LENGTH_OF_BCD;
            m.setState(pdu.getInt8(offset));
            offset++;

            target.getMembers().add(m);
        }
        return target;
    }
}
