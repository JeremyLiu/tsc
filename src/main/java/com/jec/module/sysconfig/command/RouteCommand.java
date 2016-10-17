package com.jec.module.sysconfig.command;

import com.jec.module.sysconfig.entity.Route;
import com.jec.protocol.command.Command;
import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.SNGenerator;
import com.jec.protocol.pdu.implement.IncreasedPduBuilder;

/**
 * Created by jeremyliu on 7/2/16.
 */
public class RouteCommand extends Command {

    private Route route;

    private int mainSlot;

    public RouteCommand(){

    }

    public RouteCommand(Route route, int mainSlot){
        this.route = route;
        this.mainSlot = mainSlot;
    }
    @Override
    public PDU buildRequestPdu() throws Exception {
        IncreasedPduBuilder builder = new IncreasedPduBuilder();
        builder.addInteger8(SNGenerator.nextSN());
        builder.addInteger8(PduConstants.CMD_TYPE_YWPZ);
        builder.addInteger8(PduConstants.CMD_CODE_SZCS);

        builder.addInteger8(0x1);
        builder.addInteger8(PduConstants.CARD_TYPE_MCB);
        builder.addInteger8(mainSlot);
        

        builder.addInteger8(route.getTarget());
        builder.addInteger8(route.getType1());
        builder.addInteger8(route.getSlot1());
        builder.addInteger8(route.getPort1());
        builder.addInteger8(route.getType2());
        builder.addInteger8(route.getSlot2());
        builder.addInteger8(route.getPort2());
        builder.addInteger8(route.getType3());
        builder.addInteger8(route.getSlot3());
        builder.addInteger8(route.getPort3());

        IncreasedPduBuilder head = new IncreasedPduBuilder();
        head.addInteger8(0);
        head.addInteger8(route.getNetunit());
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
