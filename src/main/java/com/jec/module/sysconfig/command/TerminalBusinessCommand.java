package com.jec.module.sysconfig.command;

import com.jec.module.sysconfig.entity.vo.Member;
import com.jec.module.sysconfig.entity.vo.TerminalBusinessView;
import com.jec.protocol.command.Command;
import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.ProtocolUtils;
import com.jec.protocol.pdu.SNGenerator;
import com.jec.protocol.pdu.implement.IncreasedPduBuilder;
import com.jec.protocol.unit.BCD;

import java.util.List;

/**
 * Created by jeremyliu on 09/10/2016.
 */
public class TerminalBusinessCommand extends Command {

    private List<TerminalBusinessView> configs;

    private int mainSlot;

    private int netId;

    public TerminalBusinessCommand(int netId, List<TerminalBusinessView> configs, int mainSlot){
        this.configs = configs;
        this.mainSlot = mainSlot;
        this.netId = netId;
    }

    public int getNetId() {
        return netId;
    }

    public void setNetId(int netId) {
        this.netId = netId;
    }

    public List<TerminalBusinessView> getConfigs() {
        return configs;
    }

    public void setConfigs(List<TerminalBusinessView> configs) {
        this.configs = configs;
    }

    public int getMainSlot() {
        return mainSlot;
    }

    public void setMainSlot(int mainSlot) {
        this.mainSlot = mainSlot;
    }

    @Override
    public PDU buildRequestPdu() throws Exception {
        if(configs.size()==0)
            throw new IllegalArgumentException("配置项为空");
        // 报内容
        IncreasedPduBuilder pbContent = new IncreasedPduBuilder();
        pbContent.addInteger8(SNGenerator.nextSN());
        pbContent.addInteger8(PduConstants.CMD_TYPE_YWPZ);
        pbContent.addInteger8(PduConstants.CMD_CODE_SZCS);
        pbContent.addInteger8(PduConstants.CONFIG_TYPE_TERMINAL_BUSINESS);
        pbContent.addInteger8(PduConstants.CARD_TYPE_MCB);
        pbContent.addInteger8(mainSlot);

        pbContent.addBCD(BCD.fromString(configs.get(0).getDeviceNumber()), PduConstants.LENGTH_OF_BCD);
        pbContent.addInteger8(configs.size());

        for(TerminalBusinessView config: configs){
            pbContent.addInteger8(config.getKeyValue());
            pbContent.addInteger8(config.getKeyType());
            pbContent.addInteger8(config.getType());
            pbContent.addBCD(BCD.fromString(config.getCode()), PduConstants.LENGTH_OF_BCD);
            pbContent.addString(config.getName(), PduConstants.LENGTH_OF_STR);
            Member[] members = config.getMemberList();
            pbContent.addInteger8(members.length);
            for(Member member: members){
                pbContent.addInteger8(member.type);
                pbContent.addBCD(BCD.fromString(member.number), PduConstants.LENGTH_OF_BCD);
            }
        }

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
