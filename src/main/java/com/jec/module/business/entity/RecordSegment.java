package com.jec.module.business.entity;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.io.File;

/**
 * Created by jeremyliu on 6/19/16.
 */
public class RecordSegment {

    @XStreamOmitField
    private Record record;

    @XStreamAsAttribute
    private int index;

    @XStreamAsAttribute
    private long startByte;

    @XStreamAsAttribute
    private long endByte;

    public RecordSegment(Record record, int index){
        this.record = record;
        this.index = index;
    }

    public String getTargetFile(){
        return record.getFilePath() + File.separator + index + ".mp3";
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public long getStartByte() {
        return startByte;
    }

    public void setStartByte(long startByte) {
        this.startByte = startByte;
    }

    public long getEndByte() {
        return endByte;
    }

    public void setEndByte(long endByte) {
        this.endByte = endByte;
    }
}
