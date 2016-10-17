package com.jec.module.sysmonitor.entity;

import com.jec.base.entity.NetState;

import java.io.Serializable;

/**
 * Created by jeremyliu on 5/10/16.
 */
public class CardState extends NetState implements Serializable {

    private NetState[] portState = new NetState[0];

    public NetState[] getPortState() {
        return portState;
    }

    public void setPortState(NetState[] portState) {
        this.portState = portState;
    }
}
