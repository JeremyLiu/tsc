package com.jec.module.business.record;

import com.jec.base.core.PipeTask;
import com.jec.module.business.entity.Record;
import com.jec.protocol.unit.BytesWrap;
import com.jec.utils.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by jeremyliu on 6/19/16.
 */
public class PcmTask extends PipeTask<BytesWrap>{

    private Record context;
    private FileOutputStream fos;

    private long lastUpdate = 0;
    private long timeout = Constants.RecordTimeOut;

    private Queue<BytesWrap> queue= new LinkedList<BytesWrap>();

    private boolean exit = true;

    public Record getContext() {
        return this.context;
    }

    public void update() {
        lastUpdate = System.currentTimeMillis();
    }


    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public boolean isTimeout() {
        return (System.currentTimeMillis() - lastUpdate) > timeout;
    }

    public void feedSession(byte[] data, int offset, int length) {


    }

    @Override
    public void run(){
        String pcmFile = context.getPcmFile();
        File file = new File(pcmFile);


        try {
            file.createNewFile();
            fos = new FileOutputStream(pcmFile);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        writePcmData();
    }

    private void writePcmData(){
        do{

            synchronized (this){
                if(queue.isEmpty())
                    try{
                        wait();
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                BytesWrap data = queue.remove();
                try {
                    fos.write(data.buffer(),data.offset(), data.length());
                }catch (IOException e){
                    e.printStackTrace();
                }

            }
        }while(!exit || !queue.isEmpty());

        try {
            if(fos != null) {
                fos.flush();
                fos.close();
                fos = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void work(BytesWrap data) {
        synchronized (this){
            queue.add(data);
        }
    }
}
