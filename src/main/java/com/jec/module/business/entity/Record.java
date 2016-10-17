package com.jec.module.business.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jec.utils.Constants;
import com.jec.utils.DateTimeUtils;
import com.jec.utils.XmlUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jeremyliu on 6/19/16.
 */
@Entity
@Table(name="zhwg_record")
public class Record implements Serializable{

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment",strategy="increment")
    private int id;

    @Column(name="calling_number")
    private String callingNumber;

    @Column(name="called_number")
    private String calledNumber;

    @Column(name="start_time")
    private Date startTime;

    @Column(name="period_time")
    private long period;

    @Column(name="file")
    private String filePath;

    @Transient
    private List<RecordSegment> segments = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCallingNumber() {
        return callingNumber;
    }

    public void setCallingNumber(String callingNumber) {
        this.callingNumber = callingNumber;
    }

    public String getCalledNumber() {
        return calledNumber;
    }

    public void setCalledNumber(String calledNumber) {
        this.calledNumber = calledNumber;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getStartTime() {
        return startTime;
    }


    public void setStartTime(Date startTime){
        this.startTime = startTime;
    }

    public void setStartTime(long startTime){
        this.startTime = new Date(startTime);
    }

    public long getPeriod() {
        return period;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    @JsonIgnore
    public String getFilePath() {
        return filePath;
    }

    @JsonIgnore
    public String getPcmFile(){
        return filePath + File.separator + "data";
    }

    @JsonIgnore
    public String getXmlFile(){
        return filePath + File.separator + "info.xml";
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFilePath(int id){
        String date = DateTimeUtils.Date2String(new Date());
        filePath = Constants.recordPath + File.separator + date + File.separator + id;
        File file = new File(filePath);
        if(!file.exists() || !file.isDirectory())
            file.mkdirs();
    }

//    public RecordSegment newSegment(long end){
//        int lastIndex = segments.size();
//        long lastByte = 0L;
//        if(lastIndex > 0)
//            lastByte = segments.get(lastIndex-1).getEndByte()+1;
//        if(end < lastByte)
//            return null;
//        RecordSegment recordSegment = new RecordSegment(this,lastIndex);
//        recordSegment.setStartByte(lastByte);
//        recordSegment.setEndByte(end);
//        return recordSegment;
//    }

    @JsonIgnore
    public List<RecordSegment> getSegments() {
        return segments;
    }

    public void setSegments(List<RecordSegment> segments) {
        this.segments = segments;
    }

    public void readConfig(){
        segments = XmlUtils.readXml(getXmlFile(),RecordSegment.class);
    }
}
