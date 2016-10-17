package com.jec.module.sysconfig.command;

import com.jec.module.sysconfig.entity.VdrConfig;
import com.jec.protocol.command.Command;
import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.ProtocolUtils;
import com.jec.protocol.pdu.SNGenerator;
import com.jec.protocol.pdu.implement.IncreasedPduBuilder;
import com.jec.protocol.unit.BCD;

/**
 * Created by jeremyliu on 08/10/2016.
 */
public class VdrCommand extends Command {

    public VdrCommand(int netId, VdrConfig config, int mainSlot){
        this.netId = netId;
        this.vdrConfig = config;
        this.mainSlot = mainSlot;
    }

    private VdrConfig vdrConfig;

    private int mainSlot;

    private int netId;

    public VdrConfig getVdrConfig() {
        return vdrConfig;
    }

    public void setVdrConfig(VdrConfig vdrConfig) {
        this.vdrConfig = vdrConfig;
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
        // 报内容
        IncreasedPduBuilder pbContent = new IncreasedPduBuilder();
        pbContent.addInteger8(SNGenerator.nextSN());
        pbContent.addInteger8(PduConstants.CMD_TYPE_YWPZ);
        pbContent.addInteger8(PduConstants.CMD_CODE_SZCS);
        pbContent.addInteger8(PduConstants.CONFIG_TYPE_VDR);
        pbContent.addInteger8(PduConstants.CARD_TYPE_MCB);
        pbContent.addInteger8(mainSlot);

        pbContent.addBCD(BCD.fromString(vdrConfig.getNumber()), PduConstants.LENGTH_OF_BCD);
        pbContent.addInteger8(vdrConfig.isEnable()? 1:0);
        pbContent.addBCD(BCD.fromString(vdrConfig.getPortNumber()), PduConstants.LENGTH_OF_BCD);

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
