package com.jec.module.sysconfig.command;

import com.jec.module.sysconfig.entity.DigitTrunk;
import com.jec.protocol.command.Command;
import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.ProtocolUtils;
import com.jec.protocol.pdu.SNGenerator;
import com.jec.protocol.pdu.implement.IncreasedPduBuilder;
import com.jec.protocol.unit.BCD;

/**
 * Created by jeremyliu on 9/26/16.
 */
public class DigitTrunkCommand extends Command {

    private DigitTrunk digitTrunk;

    private int mainSlot;

    private int netId;

    public DigitTrunkCommand(){

    }

    public DigitTrunkCommand(DigitTrunk digitTrunk, int mainSlot, int netId){
        this.digitTrunk = digitTrunk;
        this.mainSlot = mainSlot;
        this.netId = netId;
    }

    public DigitTrunk getDigitTrunk() {
        return digitTrunk;
    }

    public void setDigitTrunk(DigitTrunk digitTrunk) {
        this.digitTrunk = digitTrunk;
    }

    public int getMainSlot() {
        return mainSlot;
    }

    public void setMainSlot(int mainSlot) {
        this.mainSlot = mainSlot;
    }

    public int getNetId() {
        return netId;
    }

    public void setNetId(int netId) {
        this.netId = netId;
    }

    @Override
    public PDU buildRequestPdu() throws Exception {

        IncreasedPduBuilder pbContent = new IncreasedPduBuilder();
        pbContent.addInteger8(SNGenerator.nextSN());
        pbContent.addInteger8(PduConstants.CMD_TYPE_YWPZ);
        pbContent.addInteger8(0x01);
        pbContent.addInteger8(PduConstants.CONFIG_TYPE_DIGITTRUNK);
        pbContent.addInteger8(PduConstants.CARD_TYPE_DRB);
        pbContent.addInteger8(mainSlot);
        pbContent.addInteger8(netId);
        pbContent.addInteger8(digitTrunk.getSlot());
        pbContent.addInteger8(digitTrunk.getPort());

        //TODO: 设置opc,dpc,cic等值

        // 报文头
        IncreasedPduBuilder pb = new IncreasedPduBuilder();
        pb.addInteger8(0);
        pb.addInteger8(netId);
        pb.addInteger8(PduConstants.ID_LOCAL);
        pb.addInteger8(PduConstants.PROTOCOL_TYPE);
        pb.addInteger16(pbContent.size());
        pb.addBuilder(pbContent);

        return pb.buildPdu();
    }

    @Override
    public void processResponsePdu(PDU pdu) throws Exception {
        if(ProtocolUtils.getCmdSN(pdu) != ProtocolUtils.getCmdSN(request)) {
            error("回应指令与请求指令序号不匹配");
        }


        int cmdCode = ProtocolUtils.getCmdCode(pdu);
        if(cmdCode == PduConstants.CMD_CODE_SZSB) { // 失败

            result = false;
            errorText = String.valueOf(pdu.getInt8(PduConstants.LENGTH_OF_HEAD + PduConstants.LENGTH_OF_BCD));

        } else if( cmdCode == PduConstants.CMD_CODE_SZCG) { // 成功

            result = true;


        } else {  // 命令字错误

            errorText = "回应指令命令字错误";

        }
    }
}
