package com.jec.protocol.command;

import com.jec.base.entity.NetState;
import com.jec.module.sysmonitor.entity.NetUnit;
import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.SNGenerator;
import com.jec.protocol.pdu.implement.DefaultPduBuilder;
import com.jec.protocol.unit.BCD;

/**
 * Created by jeremyliu on 5/19/16.
 */
public class DeviceCommand extends Command {

    private int netunit;
    private BCD netunitPort;
    private int mainSlot;
    private int state = NetState.UNKNOWN;

    public DeviceCommand(int netunit,int mainslot, BCD netUnitPort){
        this.netunit = netunit;
        this.mainSlot = mainslot;
        this.netunitPort = netUnitPort;
    }

    public int getState(){
        return this.state;
    }

    @Override
    public PDU buildRequestPdu() throws Exception {
        DefaultPduBuilder builder = new DefaultPduBuilder(PduConstants.LENGTH_OF_HEAD + PduConstants.LENGTH_OF_BCD);

        int offset = 0;

        builder.setInt8(offset, 0);
        offset++;

        builder.setInt8(offset, netunit);
        offset++;

        builder.setInt8(offset, PduConstants.ID_LOCAL);
        offset++;

        builder.setInt8(offset, PduConstants.PROTOCOL_TYPE);
        offset++;

        builder.setInt16(offset, PduConstants.LENGTH_OF_BCD + 6);
        offset += 2;

        builder.setInt8(offset, SNGenerator.nextSN());
        offset++;

        builder.setInt8(offset, PduConstants.CMD_TYPE_SBJS);
        offset++;

        builder.setInt8(offset, PduConstants.CMD_CODE_PZCX);
        offset++;

        builder.setInt8(offset, PduConstants.MONITOR_NET_DEVICE);
        offset++;

        builder.setInt8(offset, PduConstants.CARD_TYPE_MCB);
        offset++;

        builder.setInt8(offset, mainSlot);
        offset++;

        builder.setBCD(offset, PduConstants.LENGTH_OF_BCD, netunitPort);
        return builder.buildPdu();
    }

    @Override
    public void processResponsePdu(PDU pdu) throws Exception {
        int offset = PduConstants.LENGTH_OF_HEAD + PduConstants.LENGTH_OF_BCD;
        state = pdu.getInt8(offset);
    }
}
