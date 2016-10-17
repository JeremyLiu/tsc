package com.jec.module.sysconfig.command;

import com.jec.module.sysconfig.entity.Drb;
import com.jec.protocol.command.Command;
import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.SNGenerator;
import com.jec.protocol.pdu.implement.IncreasedPduBuilder;

/**
 * Created by jeremyliu on 7/2/16.
 */
public class DrbCommand extends Command {

    private Drb drb;

    private int mainSlot;

    public DrbCommand(){

    }

    public DrbCommand(Drb drb, int mainSlot){
        this.drb = drb;
        this.mainSlot = mainSlot;
    }

    public Drb getDrb() {
        return drb;
    }

    public void setDrb(Drb drb) {
        this.drb = drb;
    }

    public int getMainSlot() {
        return mainSlot;
    }

    public void setMainSlot(int mainSlot) {
        this.mainSlot = mainSlot;
    }

    @Override
    public PDU buildRequestPdu() throws Exception {
        IncreasedPduBuilder builder = new IncreasedPduBuilder();
        builder.addInteger8(SNGenerator.nextSN());
        builder.addInteger8(PduConstants.CMD_TYPE_SBPZ);
        builder.addInteger8(PduConstants.CMD_CODE_SZCS);

        builder.addInteger8(0x14);
        builder.addInteger8(PduConstants.CARD_TYPE_MCB);
        builder.addInteger8(mainSlot);

        builder.addInteger8(drb.getCardType());
        builder.addInteger8(drb.getSlot());
        builder.addInteger8(drb.getPort());
        builder.addInteger8(drb.getWorkMode());
        builder.addInteger8(drb.getInterfaceMode());
        builder.addInteger8(drb.getDistanceMode());
        builder.addInteger8(drb.getClockMode());
        builder.addInteger32(drb.getOPC());
        builder.addInteger32(drb.getDPC());
        builder.addInteger16(drb.getCIC());

        IncreasedPduBuilder head = new IncreasedPduBuilder();
        head.addInteger8(0);
        head.addInteger8(drb.getNetunit());
        head.addInteger8(PduConstants.ID_LOCAL);
        head.addInteger8(PduConstants.PROTOCOL_TYPE);
        head.addInteger16(builder.size());
        head.addBuilder(builder);

        return head.buildPdu();
    }

    @Override
    public void processResponsePdu(PDU pdu) throws Exception {
        result = pdu.getInt8(8) == PduConstants.CMD_CODE_SZCG;
    }
}
