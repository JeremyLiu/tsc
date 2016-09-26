package com.jec.module.sysconfig.command;

import com.jec.module.sysconfig.entity.TonglingConfig;
import com.jec.protocol.command.Command;
import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.SNGenerator;
import com.jec.protocol.pdu.implement.IncreasedPduBuilder;
import com.jec.protocol.unit.BCD;

/**
 * Created by jeremyliu on 7/2/16.
 */
public class TonglingCommand extends Command {

    private TonglingConfig config;

    private int mainSlot;

    private int netId;

    public TonglingCommand(int netId, TonglingConfig config, int mainSlot){
        this.mainSlot = mainSlot;
        this.config = config;
        this.netId = netId;
    }


    @Override
    public PDU buildRequestPdu() throws Exception {
        // 报内容
        IncreasedPduBuilder pbContent = new IncreasedPduBuilder();
        pbContent.addInteger8(SNGenerator.nextSN());
        pbContent.addInteger8(0x03);
        pbContent.addInteger8(0x01);
        pbContent.addInteger8(0x03);
        pbContent.addInteger8(0x01);
        pbContent.addInteger8(mainSlot);

        pbContent.addBCD(BCD.fromString(config.getCode()), PduConstants.LENGTH_OF_BCD);

        pbContent.addString(config.getName(), PduConstants.LENGTH_OF_STR);

        String[] users = config.getUsers();
        String[] commanders = config.getCommanders();
        String[] members = config.getMembers();

        pbContent.addInteger8(users.length);
        for(String user: users) {
            if(!user.equals(""))
                pbContent.addBCD(BCD.fromString(user), PduConstants.LENGTH_OF_BCD);
        }

        pbContent.addInteger8(commanders.length);
        for(String commander: commanders) {
            if(!commander.equals(""))
                pbContent.addBCD(BCD.fromString(commander), PduConstants.LENGTH_OF_BCD);
        }

        pbContent.addInteger8(members.length);
        for(String member: members) {
            if(!member.equals(""))
                pbContent.addBCD(BCD.fromString(member), PduConstants.LENGTH_OF_BCD);
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
        if(pdu.getInt8(6) != request.getInt8(6)) {
            errorText="回应指令与请求指令序号不匹配";
        }

        int cmdCode = pdu.getInt8(8);
        if(cmdCode == 3) { // 失败

            result = false;
            errorText = String.valueOf(pdu.getInt8(22));

        } else if( cmdCode == 2) { // 成功

            result = true;

        } else {  // 命令字错误

            errorText="回应指令命令字错误";
        }
    }
}
