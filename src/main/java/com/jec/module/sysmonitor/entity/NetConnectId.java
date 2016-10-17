package com.jec.module.sysmonitor.entity;

import java.io.Serializable;

/**
 * Created by jeremyliu on 5/21/16.
 */
public class NetConnectId implements Serializable {

    private int srcId;

    private int destId;

    public NetConnectId(){

    }

    public NetConnectId(int srcId, int destId){
        this.srcId = srcId;
        this.destId = destId;
    }

    public int getSrcId() {
        return srcId;
    }

    public void setSrcId(int srcId) {
        this.srcId = srcId;
    }

    public int getDestId() {
        return destId;
    }

    public void setDestId(int destId) {
        this.destId = destId;
    }

    @Override
    public boolean equals(Object other){
        if(other == this)
            return true;
        if(other == null || !(other instanceof NetConnectId))
            return false;

        NetConnectId netConnectId = (NetConnectId) other;
        return this.srcId == netConnectId.srcId && this.destId == netConnectId.destId;
    }
}
