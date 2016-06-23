package com.jec.module.business.record;

import com.jec.base.core.PipeTask;
import com.jec.module.business.entity.Record;
import com.jec.module.business.entity.RecordSegment;
import com.jec.protocol.unit.BytesWrap;
import com.jec.utils.Constants;
import com.jec.utils.XmlUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by jeremyliu on 6/19/16.
 */
public class RecordSession extends Thread{

    private long timeout = Constants.RecordTimeOut;

    private Queue<BytesWrap> queue= new LinkedList<>();

    private List<RecordSessionListener> listeners = new ArrayList<>();

    private Record record;

    private boolean exit = true;

    private PcmTask pcmTask;

    private int sessionId;


    public RecordSession(int id, String callingNumber, String calledNumber){
        record = new Record();
        record.setFilePath(id);

        record.setCallingNumber(callingNumber);
        record.setCalledNumber(calledNumber);
        pcmTask = new PcmTask(record.getPcmFile(), true);
        this.sessionId = id;
    }

    public Record getRecord(){
        return record;
    }

    public int getSessionId(){
        return sessionId;
    }

    public void addListener(RecordSessionListener listener){
        if(listener != null)
            listeners.add(listener);
    }

    public void addData(BytesWrap data){
        synchronized (this) {
            queue.add(data);
            notify();
        }
    }

    public void openSession(){
        exit = false;
        record.setStartTime(System.currentTimeMillis());
        this.start();
    }

    public synchronized void closeSession(){
        exit = true;
    }

    private void notifyClose(){
        for(RecordSessionListener listener: listeners)
            listener.onSessionClosed(this);
    }

    @Override
    public void run(){
        if(!pcmTask.prepare()){
            exit = true;
            notifyClose();
            return;
        }
        do{
            synchronized (this){
                if(queue.isEmpty()) {
                    long waitStart = System.currentTimeMillis();
                    try {
                        wait(timeout+50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //超时
                    if(System.currentTimeMillis() - waitStart >= timeout)
                        break;
                }
                BytesWrap data = queue.remove();
                //数据为0 或者写入失败结束session
                if(data.length() == 0 || !pcmTask.work(data))
                    break;
                record.setPeriod(pcmTask.getRecordTime());
            }
        }while(!exit || !queue.isEmpty());

        pcmTask.done();

        List<RecordSegment> segments = pcmTask.getRecordSegmentList();
        record.setSegments(segments);

        //压缩编码
        for(RecordSegment segment : segments) {
            String pcmFile = segment.getOriginFile();
            File file = new File(pcmFile);

            //编码成功后删除原始文件
            if(RecordEncode.encode(file.getAbsolutePath(), segment.getTargetFile())>0){
                file.delete();
            }
        }

        //写配置文件
        if(XmlUtils.writeXml(record.getXmlFile(),segments,RecordSegment.class))
            notifyClose();
    }
}
