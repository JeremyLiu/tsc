package com.jec.module.sysmonitor.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.io.Serializable;

/**
 * Created by jeremyliu on 5/24/16.
 */
@XStreamAlias("NEFaultInfo")
public class NEFaultInfo implements Serializable{

    @XStreamAsAttribute
    @XStreamAlias("NEID")
    private int netId;

    @XStreamAsAttribute
    @XStreamAlias("FaultLevel")
    private int faultLevel;

    @XStreamAsAttribute
    @XStreamAlias("FaultCode")
    private int faultCode;

    @XStreamAsAttribute
    @XStreamAlias("FaultDesp")
    private String faultDesp;

    public int getNetId() {
        return netId;
    }

    public void setNetId(int netId) {
        this.netId = netId;
    }

    public int getFaultLevel() {
        return faultLevel;
    }

    public void setFaultLevel(int faultLevel) {
        this.faultLevel = faultLevel;
    }

    public int getFaultCode() {
        return faultCode;
    }

    public void setFaultCode(int faultCode) {
        this.faultCode = faultCode;
    }

    public String getFaultDesp() {
        return faultDesp;
    }

    public void setFaultDesp(String faultDesp) {
        this.faultDesp = faultDesp;
    }
}
