package com.jec.module.sysconfig.command;

import com.jec.module.sysconfig.entity.Clock;
import com.jec.protocol.command.Command;
import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.SNGenerator;
import com.jec.protocol.pdu.implement.IncreasedPduBuilder;

/**
 * Created by jeremyliu on 7/2/16.
 */
public class ClockCommand extends Command {

    private Clock clock;

    private int mainSlot;

    private int netId;

    public ClockCommand(int netId, Clock clock, int mainSlot){
        this.clock = clock;
        this.mainSlot = mainSlot;
        this.netId = netId;
    }

    public int getMainSlot() {
        return mainSlot;
    }

    public void setMainSlot(int mainSlot) {
        this.mainSlot = mainSlot;
    }

    public Clock getClock() {
        return clock;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    public int getNetId() {
        return netId;
    }

    public void setNetId(int netId) {
        this.netId = netId;
    }

    @Override
    public PDU buildRequestPdu() throws Exception {
        IncreasedPduBuilder builder = new IncreasedPduBuilder();
        builder.addInteger8(SNGenerator.nextSN());
        builder.addInteger8(PduConstants.CMD_TYPE_SBPZ);
        builder.addInteger8(PduConstants.CMD_CODE_SZCS);

        builder.addInteger8(0x1);
        builder.addInteger8(PduConstants.CARD_TYPE_MCB);
        builder.addInteger8(mainSlot);

        builder.addInteger8(clock.getType1());
        builder.addInteger8(clock.getSlot1());
        builder.addInteger8(clock.getPort1());
        builder.addInteger8(clock.getType2());
        builder.addInteger8(clock.getSlot2());
        builder.addInteger8(clock.getPort2());
        builder.addInteger8(clock.getType3());
        builder.addInteger8(clock.getSlot3());
        builder.addInteger8(clock.getPort3());

        IncreasedPduBuilder head = new IncreasedPduBuilder();
        head.addInteger8(0);
        head.addInteger8(netId);
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
