package com.jec.base.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Created by jeremyliu on 5/10/16.
 */
public class NetState implements Serializable{

    public static final int UNKNOWN = 0;
    public static final int US_NORMAL = 1;
    public static final int US_FAILURE = 2;
    public static final int US_OUTLINE = 3;


    public static final int CS_NORMAL = 0;
    public static final int CS_FAILURE = 1;

    public static final int PS_DISABLED = -2;
    public static final int PS_NORMAL = 0;
    public static final int PS_FAILURE = 1;


    public static final int DS_NORMAL = 1;
    public static final int DS_LOCAL_CTRL = 2;
    public static final int DS_FAILURE = 3;

    public static int cardStateMap(int state){
        switch(state){
            case 0: return US_NORMAL;
            case 1: return US_FAILURE;
            default: return UNKNOWN;
        }
    }

    public static int deviceStateMap(int state){
        switch (state){
            case 0: return US_NORMAL;
            case 1: return US_FAILURE;
            default: return UNKNOWN;
        }
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
    @JsonIgnore
    public int getNetunit() {
        return netunit;
    }

    public void setNetunit(int netunit) {
        this.netunit = netunit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    protected int state = UNKNOWN;

    protected int netunit = 0;

    protected int id;

    public void from(NetState netState){
        this.state = netState.state;
    }

    public boolean equalWith(NetState netState){
        return this.equals(netState);
    }
}
