package com.jec.module.business.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.io.File;

/**
 * Created by jeremyliu on 6/19/16.
 */
@XStreamAlias("RecordSegment")
public class RecordSegment {

    @XStreamOmitField
    private String basePath;

    @XStreamAsAttribute
    private int index;

    @XStreamAsAttribute
    private long period;

    @XStreamAsAttribute
    private String targetFile;

    public RecordSegment(){

    }

    public RecordSegment(String basePath, int index){
        this.basePath = basePath;
        this.index = index;
        targetFile = basePath + "-" + index + ".mp3";
    }

    public String getTargetFile(){
        return targetFile;
    }

    public void setTargetFile(String targetFile) {
        this.targetFile = targetFile;
    }

    public String getOriginFile(){
        return basePath + "-" + index + ".pcm";
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }
}
