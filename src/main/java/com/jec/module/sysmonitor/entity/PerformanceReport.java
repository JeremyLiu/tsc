package com.jec.module.sysmonitor.entity;

import com.jec.base.entity.MessageHead;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.Date;

/**
 * Created by jeremyliu on 5/24/16.
 */
public class PerformanceReport {

    @XStreamAlias("StartDaytime")
    private String startDaytime = MessageHead.formatter.format(new Date());

    @XStreamAlias("EndDaytime")
    private String endDaytime = MessageHead.formatter.format(new Date());

    @XStreamAlias("LostRate")
    private LostRate lostRate;

    public PerformanceReport(){

    }

    public PerformanceReport(int rate){
        lostRate = new LostRate();
        lostRate.setRate(rate);
    }

    public String getStartDaytime() {
        return startDaytime;
    }

    public void setStartDaytime(String startDaytime) {
        this.startDaytime = startDaytime;
    }

    public String getEndDaytime() {
        return endDaytime;
    }

    public void setEndDaytime(String endDaytime) {
        this.endDaytime = endDaytime;
    }

    public LostRate getLostRate() {
        return lostRate;
    }

    public void setLostRate(LostRate lostRate) {
        this.lostRate = lostRate;
    }
}
