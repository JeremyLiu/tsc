package com.jec.module.sysconfig.command;

import com.jec.protocol.command.Command;
import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.SNGenerator;
import com.jec.protocol.pdu.implement.IncreasedPduBuilder;

import java.security.Timestamp;
import java.util.Calendar;

/**
 * Created by jeremyliu on 7/4/16.
 */
public class TimeCommand extends Command{

    private int netunit;

    private int mainSlot;

    public TimeCommand(){

    }
    private long ts;

    public TimeCommand(int netunit, int mainSlot,long ts){
        this.netunit = netunit;
        this.mainSlot = mainSlot;
        this.ts = ts;
    }

    @Override
    public PDU buildRequestPdu() throws Exception {
        IncreasedPduBuilder builder = new IncreasedPduBuilder();
        builder.addInteger8(SNGenerator.nextSN());
        builder.addInteger8(PduConstants.CMD_TYPE_SBPZ);
        builder.addInteger8(PduConstants.CMD_CODE_SZCS);
        builder.addInteger8(0x02);

        builder.addInteger8(PduConstants.CARD_TYPE_MCB);
        builder.addInteger8(mainSlot);

        Calendar calendar = Calendar.getInstance();
        if(ts>=0)
            calendar.setTimeInMillis(ts);

        builder.addInteger8(calendar.get(Calendar.YEAR) - 2000);
        builder.addInteger8(calendar.get(Calendar.MONTH) + 1);
        builder.addInteger8(calendar.get(Calendar.DAY_OF_MONTH));
        builder.addInteger8(calendar.get(Calendar.HOUR_OF_DAY));
        builder.addInteger8(calendar.get(Calendar.MINUTE));
        builder.addInteger8(calendar.get(Calendar.SECOND));

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
        if(request.getInt8(6) != pdu.getInt8(6)) {
            result = false;
            errorText = "指令回应与指令请求不匹配";
        }else
            result = (pdu.getInt8(8) == PduConstants.CMD_CODE_SZCG);
    }
}
