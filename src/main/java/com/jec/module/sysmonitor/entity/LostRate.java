package com.jec.module.sysmonitor.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Created by jeremyliu on 5/24/16.
 */
@XStreamAlias("LostRate")
public class LostRate {

    @XStreamAsAttribute
    @XStreamAlias("Rate")
    private int rate;

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }
}
