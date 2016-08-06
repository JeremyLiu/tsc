package com.jec.module.business.service;

import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import com.jec.base.annotation.SysLog;
import com.jec.module.business.dao.RecordDao;
import com.jec.module.business.entity.Record;
import com.jec.module.business.entity.RecordSegment;
import com.jec.module.business.record.RecordSession;
import com.jec.module.business.record.RecordSessionListener;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.processor.RawProcessor;
import com.jec.protocol.unit.BCD;
import com.jec.protocol.unit.BytesWrap;
import com.jec.utils.FileUtil;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.net.DatagramPacket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jeremyliu on 6/18/16.
 */
@Service
@Scope
public class RecordService implements RawProcessor,RecordSessionListener{

    @Resource
    private RecordDao recordDao;

    private Map<Integer,RecordSession> sessionMap = new ConcurrentHashMap<>();

    public RecordSession route(int id,String callingNum, String calledNum, BytesWrap data){
        RecordSession session;
        if(sessionMap.containsKey(id))
            session = sessionMap.get(id);
        else {
            session = new RecordSession(id, callingNum, calledNum);
            session.addListener(this);
            session.openSession();
            sessionMap.put(id,session);
        }
        session.addData(data);
        return session;
    }

    public List<Record> getCurrentRecord(){
        List<Record> records = new ArrayList<>();
        for(RecordSession session : sessionMap.values()){
            records.add(session.getRecord());
        }
        return records;
    }

    @Transactional(readOnly = true)
    public List<RecordSegment> getRecordSegment(int id, boolean recording){
        if(recording){
            RecordSession session =  sessionMap.get(id);
            if(session == null)
                return null;
            else
                return session.getRecordSegement();
        }else{
            Record record = recordDao.find(id);
            record.readConfig();
            return record.getSegments();
        }
    }

    @Transactional
    public void addRecord(Record record){
        recordDao.save(record);
    }

    @Transactional(readOnly = true)
    public List<Record> getRecord(Date startTime, Date endTime, String key, int offset, int pageSize){
        Search search = new Search(Record.class);
        search.addSort("startTime", true);
        if(startTime != null)
            search.addFilterGreaterOrEqual("startTime",startTime);
        if(startTime != null)
            search.addFilterLessOrEqual("endTime",endTime);
        if(key != null && !key.equals("")){
            key = "%" + key + "%";
            Filter callingFilter = new Filter("callingNumber",key,Filter.OP_ILIKE);
            Filter calledFilter = new Filter("calledNumber", key, Filter.OP_ILIKE);
            search.addFilter(Filter.or(callingFilter,calledFilter));
        }
        search.setFirstResult(offset);
        search.setMaxResults(pageSize);
        List<Record> records=  recordDao.search(search);
        return records;
    }

    @Transactional
    public boolean removeRecord(int id){
        Record record = recordDao.find(id);
        if(FileUtil.deleteDir(record.getFilePath())){
            recordDao.remove(record);
            return true;
        }else
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
        String callingStr = callingNum == null ? "": callingNum.toString();
        String calledStr = calledNum == null ? "": calledNum.toString();
        bytes.setOff(26,used);
        route(id,callingStr,calledStr,bytes);
        return true;
    }

    @Override
    public void onSessionClosed(RecordSession session) {
        int sessionId = session.getSessionId();
        Record record = session.getRecord();
        synchronized(this){
            sessionMap.remove(sessionId);
        }
        if(record.getSegments().size()>0) {
            TransactionSynchronizationManager.initSynchronization();
            recordDao.saveForce(record);
        }
    }
}
