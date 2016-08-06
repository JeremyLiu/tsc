package com.jec.protocol.command;

import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.SNGenerator;
import com.jec.protocol.pdu.implement.DefaultPduBuilder;

/**
 * Created by jeremyliu on 7/23/16.
 */
public class SlotCommand extends Command {

    private int netunit;

    private int type;

    private int slot;

    private int mainSlot;

    public SlotCommand(int netunit, int type, int slot, int mainSlot){
        this.netunit = netunit;
        this.type = type;
        this.slot = slot;
        this.mainSlot = mainSlot;
        this.needResponse = false;
    }

    public void setNetunit(int netunit, int mainSlot){
        this.netunit = netunit;
        this.mainSlot = mainSlot;
    }

    public void setSlot(int slot, int type){
        this.slot = slot;
        this.type = type;
    }

    @Override
    public PDU buildRequestPdu() throws Exception {
        DefaultPduBuilder builder = new DefaultPduBuilder(PduConstants.LENGTH_OF_HEAD + 2);
        int offset = 0;

        builder.setInt8(offset, 0);
        offset++;

        builder.setInt8(offset, netunit);
        offset++;

        builder.setInt8(offset, PduConstants.ID_LOCAL);
        offset++;

        builder.setInt8(offset, PduConstants.PROTOCOL_TYPE);
        offset++;

        builder.setInt16(offset, 8);
        offset += 2;

        builder.setInt8(offset, SNGenerator.nextSN());
        offset++;

        builder.setInt8(offset, PduConstants.CMD_TYPE_SBJS);
        offset++;

        builder.setInt8(offset, PduConstants.CMD_CODE_PZCX);
        offset++;

        builder.setInt8(offset, 0x01);
        offset++;

        builder.setInt8(offset, PduConstants.CARD_TYPE_MCB);
        offset++;

        builder.setInt8(offset, mainSlot);
        offset++;

        builder.setInt8(offset, type);
        offset++;

        builder.setInt8(offset, slot);
        return builder.buildPdu();
    }

    @Override
    public void processResponsePdu(PDU pdu) throws Exception {

    }
}
