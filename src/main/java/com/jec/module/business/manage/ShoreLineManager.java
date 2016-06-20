package com.jec.module.business.manage;

import com.jec.module.business.entity.Business;
import com.jec.module.business.entity.ShoreLine;
import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.ProtocolUtils;
import com.jec.utils.Constants;

/**
 * Created by jeremyliu on 6/18/16.
 */
public class ShoreLineManager extends BusinessManager<ShoreLine>{

    private int type;

    public ShoreLineManager(int type){
        String name = "";
        int config = -1;

        if(type == ShoreLine.TYPE_SR){
            name = "模拟中继";
            config = PduConstants.MONITOR_BUSINESS_SIMULATETRUNK;
        }else if(type == ShoreLine.TYPE_DR){
            name = "数字中继";
            config = PduConstants.MONITOR_BUSINESS_DIGITALTRUNK;
        }
        this.type = type;
        business = new Business(name, Constants.DefaultBusinessImg, config);
    }

    @Override
    protected ShoreLine pduToEntry(PDU pdu) {
        ShoreLine target = new ShoreLine();
        target.setType(type);
        target.setNetunit(ProtocolUtils.getSourId(pdu));

        int offset = PduConstants.LENGTH_OF_HEAD;

        target.setSlot(pdu.getInt8(offset));
        offset++;

        target.setTs(pdu.getInt8(offset));
        offset++;

        target.setShoreNumber(pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD));
        offset += PduConstants.LENGTH_OF_BCD;

        target.shoreState = pdu.getInt8(offset);
        offset++;

        target.setNumber(pdu.getBCD(offset, PduConstants.LENGTH_OF_BCD));
        offset += PduConstants.LENGTH_OF_BCD;

        target.setState(pdu.getInt8(offset));
        return target;
    }
}
