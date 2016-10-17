package com.jec.module.sysconfig.command;

import com.jec.module.sysconfig.entity.NumberEntry;
import com.jec.protocol.command.Command;
import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.SNGenerator;
import com.jec.protocol.pdu.implement.IncreasedPduBuilder;
import com.jec.protocol.unit.BCD;
import com.jec.utils.DateTimeUtils;

import java.util.Calendar;

/**
 * Created by jeremyliu on 02/10/2016.
 */
public class NumberEntryCommand extends Command {

    private NumberEntry numberEntry;

    private int mainSlot;

    private int netId;

    private int targetCardType;

    public NumberEntryCommand(){

    }

    public NumberEntryCommand(NumberEntry numberEntry, int netId, int mainSlot, int targetCardType){
        this.numberEntry = numberEntry;
        this.netId = netId;
        this.mainSlot = mainSlot;
        this.targetCardType = targetCardType;
    }

    public NumberEntry getNumberEntry() {
        return numberEntry;
    }

    public void setNumberEntry(NumberEntry numberEntry) {
        this.numberEntry = numberEntry;
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

    public int getTargetCardType() {
        return targetCardType;
    }

    public void setTargetCardType(int targetCardType) {
        this.targetCardType = targetCardType;
    }

    @Override
    public PDU buildRequestPdu() throws Exception {

        IncreasedPduBuilder builder = new IncreasedPduBuilder();
        builder.addInteger8(SNGenerator.nextSN());
        builder.addInteger8(PduConstants.CMD_TYPE_YWPZ);
        builder.addInteger8(PduConstants.CMD_CODE_SZCS);
        builder.addInteger8(PduConstants.CONFIG_TYPE_USERDATE);
        builder.addInteger8(PduConstants.CARD_TYPE_MCB);
        builder.addInteger8(mainSlot);

        builder.addInteger8(targetCardType);
        builder.addInteger8(numberEntry.getSlot());
        builder.addInteger16(numberEntry.getTs());
        builder.addBCD(BCD.fromString(numberEntry.getNumber()), PduConstants.LENGTH_OF_BCD);

        builder.addInteger8(numberEntry.getCallRight());
        builder.addInteger8(numberEntry.getTermType());
        builder.addInteger8(numberEntry.getUserType());
        builder.addInteger8(numberEntry.getUserLevel());

        builder.addInteger8(numberEntry.isRecordRight() ? 1 : 0);
        builder.addInteger8(numberEntry.isRecordEnable() ? 1 : 0);

        builder.addInteger8(numberEntry.isHotlineRight() ? 1 : 0);
        builder.addInteger8(numberEntry.isHotlineEnable() ? 1 : 0);
        builder.addBCD(BCD.fromString(numberEntry.getHotlineNumber()), PduConstants.LENGTH_OF_BCD);

        builder.addInteger8(numberEntry.isBusingcfRight() ? 1 : 0);
        builder.addInteger8(numberEntry.isBusingcfEnable() ? 1 : 0);
        builder.addBCD(BCD.fromString(numberEntry.getBusingcfNumber()), PduConstants.LENGTH_OF_BCD);

        builder.addInteger8(numberEntry.isFollowcfRight()? 1 : 0);
        builder.addInteger8(numberEntry.isFollowcfEnable()? 1: 0);
        builder.addBCD(BCD.fromString(numberEntry.getFollowcfNumber()), PduConstants.LENGTH_OF_BCD);

        builder.addInteger8(numberEntry.isNohinderRight() ? 1 : 0);
        builder.addInteger8(numberEntry.isNohinderEnable() ? 1 : 0);

        builder.addInteger8(numberEntry.isReminderRight() ? 1 : 0);
        builder.addInteger8(numberEntry.isReminderEnable() ? 1 : 0);
        Long time =  DateTimeUtils.String2TimeStamp("2016-01-01 " + numberEntry.getReminderTime());
        if(time != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(time);
            builder.addInteger8(calendar.get(Calendar.HOUR_OF_DAY));
            builder.addInteger8(calendar.get(Calendar.MINUTE));
            builder.addInteger8(calendar.get(Calendar.SECOND));
        }else{
            builder.addInteger8(0);
            builder.addInteger8(0);
            builder.addInteger8(0);
        }

        builder.addInteger8(numberEntry.isPhonestationRight() ? 1 : 0);
        builder.addInteger8(numberEntry.isPhonestationEnable() ? 1 : 0);

        builder.addInteger8(numberEntry.isVolatileMeetRight() ? 1 : 0);
        builder.addInteger8(numberEntry.isDigitRelayRight() ? 1 : 0);
        builder.addInteger8(numberEntry.isSimulateRelayRight() ? 1 : 0);


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
        if(!result)
            errorText = String.valueOf(pdu.getInt8(12 + 4));
    }
}
