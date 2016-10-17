package com.jec.module.sysconfig.command;

import com.jec.protocol.command.Command;
import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.SNGenerator;
import com.jec.protocol.pdu.implement.IncreasedPduBuilder;

/**
 * Created by jeremyliu on 7/1/16.
 * 下载板卡类型配置命令
 */
public class CardTypeCommand extends Command {

    private int netunit;
    private int mainSlot;
    private int destSlot;
    private int destType;

    public CardTypeCommand(int netunit, int mainSlot, int destSlot, int destType){
        this.netunit = netunit;
        this.mainSlot = mainSlot;
        this.destSlot = destSlot;
        this.destType = destType;
    }

    public int getNetunit() {
        return netunit;
    }

    public void setNetunit(int netunit) {
        this.netunit = netunit;
    }

    public int getMainSlot() {
        return mainSlot;
    }

    public void setMainSlot(int mainSlot) {
        this.mainSlot = mainSlot;
    }

    public int getDestSlot() {
        return destSlot;
    }

    public void setDestSlot(int destSlot) {
        this.destSlot = destSlot;
    }

    public int getDestType() {
        return destType;
    }

    public void setDestType(int destType) {
        this.destType = destType;
    }

    @Override
    public PDU buildRequestPdu() throws Exception {
        IncreasedPduBuilder builder = new IncreasedPduBuilder();
        builder.addInteger8(SNGenerator.nextSN());
        builder.addInteger8(PduConstants.CMD_TYPE_SBPZ);
        builder.addInteger8(PduConstants.CMD_CODE_SZCS);
        builder.addInteger8(0x03);
        builder.addInteger8(PduConstants.CARD_TYPE_MCB);
        builder.addInteger8(mainSlot);

        builder.addInteger8(destSlot);
        builder.addInteger8(destType);

        IncreasedPduBuilder head = new IncreasedPduBuilder();
        head.addInteger8(0);
        head.addInteger8(netunit);
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
