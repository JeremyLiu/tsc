package com.jec.module.sysmonitor.entity;

import com.jec.base.entity.NetState;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Created by jeremyliu on 5/20/16.
 */
@XStreamAlias("NETopoInfo")
public class ConnectState extends NetConnect{

    @XStreamAlias("ConnectState")
    @XStreamAsAttribute
    private int state = NetState.US_OUTLINE;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public static ConnectState from(NetConnect netConnect){
        ConnectState connectState = new ConnectState();
        connectState.setSrcId(netConnect.getSrcId());
        connectState.setDestId(netConnect.getDestId());
        return connectState;
    }
}
