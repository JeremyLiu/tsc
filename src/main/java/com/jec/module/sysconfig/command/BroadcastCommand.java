package com.jec.module.sysconfig.command;

import com.jec.module.sysconfig.entity.BroadcastConfig;
import com.jec.protocol.command.Command;
import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.SNGenerator;
import com.jec.protocol.pdu.implement.IncreasedPduBuilder;
import com.jec.protocol.unit.BCD;

/**
 * Created by jeremyliu on 05/10/2016.
 */
public class BroadcastCommand extends Command {

    private BroadcastConfig config;

    private int mainSlot;

    private int netId;

    public BroadcastCommand(){

    }

    public BroadcastCommand(BroadcastConfig config, int mainSlot, int netId){
        this.config = config;
        this.mainSlot = mainSlot;
        this.netId = netId;
    }

    public BroadcastConfig getConfig() {
        return config;
    }

    public void setConfig(BroadcastConfig config) {
        this.config = config;
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
        pbContent.addInteger8(PduConstants.CONFIG_TYPE_BROADCAST);
        pbContent.addInteger8(PduConstants.CARD_TYPE_MCB);
        pbContent.addInteger8(mainSlot);

        pbContent.addBCD(BCD.fromString(config.getCode()), PduConstants.LENGTH_OF_BCD);

        pbContent.addString(config.getName(), PduConstants.LENGTH_OF_STR);

        String[] users = config.getUsers();
        pbContent.addInteger8(users.length);
        for(int i = 0; i < users.length; i++) {
            pbContent.addBCD(BCD.fromString(users[i]), PduConstants.LENGTH_OF_BCD);
        }

        pbContent.addBCD(BCD.fromString(config.getBroadcaster()), PduConstants.LENGTH_OF_BCD);

        String[] members = config.getUsers();
        pbContent.addInteger8(members.length);
        for(int i = 0; i < members.length; i++) {
            pbContent.addBCD(BCD.fromString(members[i]), PduConstants.LENGTH_OF_BCD);
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

        int cmdCode = pdu.getInt8(8);
        if(cmdCode == 3) { // 失败

            result = false;
            errorText = String.valueOf(pdu.getInt8(22));

        } else if( cmdCode == 2) { // 成功

            result= true;

        }
    }
}
