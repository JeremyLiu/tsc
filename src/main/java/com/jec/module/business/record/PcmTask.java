package com.jec.module.business.record;

import com.jec.base.core.PipeTask;
import com.jec.module.business.entity.RecordSegment;
import com.jec.protocol.unit.BytesWrap;
import org.tritonus.share.sampled.TConversionTool;

import javax.swing.text.Segment;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by jeremyliu on 6/19/16.
 */
public class PcmTask extends PipeTask<BytesWrap>{

    private FileOutputStream fos;

    private String pcmFile;

    private long recordByte = 0;

    private long lastSegByte = 0;

    private boolean isSegment = false;

    private byte[] buffer = new byte[2048];

    private List<RecordSegment> recordSegmentList = new ArrayList<>();

    private final static long segmentSize = 5 * 1024 * 1024;


    public PcmTask(String file, boolean isSegment){
        int	ind = file.lastIndexOf(".");
        if (ind == -1
                || ind == file.length()
                || file.lastIndexOf(File.separator) > ind) {
            // when dot is at last position,
            // or a slash is after the dot, there isn't an extension
            pcmFile =  file;
        }else
            pcmFile = file.substring(0, ind);
        this.isSegment = isSegment;
    }

    public long getRecordTime(){
        return recordByte/16;
    }

    public List<RecordSegment> getRecordSegmentList(){
        return recordSegmentList;
    }

    @Override
    public boolean prepare(){
        try {
            createSegement();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private RecordSegment createSegement() throws IOException{
        int size = recordSegmentList.size();
        RecordSegment segment = new RecordSegment(pcmFile,size);
        recordSegmentList.add(segment);
        String originFile = segment.getOriginFile();
        File file = new File(originFile);
        file.getAbsolutePath();
        fos = new FileOutputStream(originFile);
        return segment;
    }

    @Override
    public boolean done(){

        try {
            if(fos != null) {
                fos.flush();
                fos.close();
                RecordSegment recordSegment = recordSegmentList.get(recordSegmentList.size()-1);
                recordSegment.setPeriod(lastSegByte/16);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean work(BytesWrap data) {
        try {
            int offset = data.offset();
            int length = data.length();

            TConversionTool.alaw2pcm16(data.buffer(), offset, buffer, 0, length, true);
            length *=2;
            //如果设置分段
            if(isSegment)
                segment(length);

            fos.write(buffer, 0, length);
            recordByte += length;
            lastSegByte += length;
            return true;
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

    private void segment(int length) throws IOException{
        if(lastSegByte + length > segmentSize){
            done();
            createSegement();
            lastSegByte = 0;
        }
    }
}
