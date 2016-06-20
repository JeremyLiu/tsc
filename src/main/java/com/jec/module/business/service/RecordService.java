package com.jec.module.business.service;

import com.jec.module.business.entity.Record;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.processor.RawProcessor;
import com.jec.protocol.unit.BCD;
import com.jec.protocol.unit.BytesWrap;
import com.jec.utils.Constants;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.net.DatagramPacket;

/**
 * Created by jeremyliu on 6/18/16.
 */
@Service
@Scope
public class RecordService implements RawProcessor{

    private long lastRecordSession = -1;

    private boolean checkTimeout(){
        if(lastRecordSession <= 0)
            return true;
        long curTime = System.currentTimeMillis();
        if(curTime-lastRecordSession > Constants.RecordTimeOut)
            return true;
        else
            return false;
    }

    @Override
    public boolean process(DatagramPacket packet) {

        byte[] data = packet.getData();
        int offset = packet.getOffset();
        int length = packet.getLength();

        BytesWrap bytes = new BytesWrap(data, offset, length);

        if(length == 4) { // 内部控制数据，收到该消息关闭所有Store
            return true;
        }else if(length != 1050) // 录音数据
			return false;

        /*
         * 检查数据头
         */
        if(0x27 != bytes.getInt8(0)) {
            return false;
        }

        if(0x24 != bytes.getInt8(1)) {
            return false;
        }

        if(0x27 != bytes.getInt8(2)) {
            return false;
        }

        if(0x26 != bytes.getInt8(3)) {
            return false;
        }
        int id  = bytes.getInt16(4);

        //System.out.println("Record head: " + HexUtils.toHex(data, 26));

        BCD callingNum = bytes.getBCD(6, PduConstants.LENGTH_OF_BCD);
        BCD calledNum = bytes.getBCD(15, PduConstants.LENGTH_OF_BCD);
        int used  = bytes.getInt16(24);

        //
        if(used > 1024 || used < 0) {
            System.out.println("录音数据长度错误：" + used);
            return false;
        }
        return true;
    }
}
