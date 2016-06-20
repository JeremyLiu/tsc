package com.jec.protocol.command;

import com.jec.module.sysmonitor.entity.NetUnit;
import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.ProtocolUtils;
import com.jec.protocol.pdu.SNGenerator;
import com.jec.protocol.pdu.implement.IncreasedPduBuilder;
import com.jec.utils.NetWorkUtil;

/**
 * Created by jeremyliu on 5/10/16.
 */
public class PduCommand extends Command{

    private int netunit = 0;
    private int mainCardSlot = 0;
    private boolean force = false;
    private boolean result = false;
    private PDU request = null;

    public PduCommand(int netunit, int mainCardSlot, boolean force) {
        super();
        this.mainCardSlot = mainCardSlot;
        this.netunit = netunit;
        this.force = force;
    }

    public boolean getResult() {
        return result;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    PDU getRequest() {

        if(request == null) {

            String host = NetWorkUtil.getLocalHost();

            IncreasedPduBuilder builder = new IncreasedPduBuilder();
            builder.addInteger8(SNGenerator.nextSN());
            builder.addInteger8(PduConstants.CMD_TYPE_REPORT_ADDR);
            builder.addInteger8(PduConstants.CMD_CODE_REPORT_ADDR_REG);
            builder.addInteger8(0x00);
            builder.addInteger8(PduConstants.CARD_TYPE_MCB);
            builder.addInteger8(mainCardSlot);

            builder.addString(host, 20);
            builder.addInteger16(NetWorkUtil.listenPort);
            builder.addInteger8(force ? 1 : 0);

            IncreasedPduBuilder head = new IncreasedPduBuilder();
            head.addInteger8(0);
            head.addInteger8(netunit);
            head.addInteger8(PduConstants.ID_LOCAL);
            head.addInteger8(PduConstants.PROTOCOL_TYPE);
            head.addInteger16(builder.size());
            head.addBuilder(builder);
            request = head.buildPdu();
        }

        return request;
    }

    @Override
    public PDU buildRequestPdu() {
        return getRequest();
    }

    @Override
    public void processResponsePdu(PDU pdu) throws Exception {
        System.out.println();
        int code = ProtocolUtils.getCmdCode(pdu);
        result = (code == PduConstants.CMD_CODE_REPORT_ADDR_REG_ACK);
    }
}
