package com.jec.module.sysmonitor.vo;

import com.jec.module.sysmonitor.entity.view.TerminalDeviceView;

import java.io.Serializable;

/**
 * Created by jeremyliu on 5/10/16.
 */
public class Device implements Serializable {

    protected static final long serialVersionUID = -2264997227937470104L;

    public Device(){

    }

    public Device(TerminalDeviceView terminal){
        this.id = terminal.getId();
        this.name = terminal.getName();
        this.port = terminal.getPortNumber();
//        this.slot = terminal.getSlotNumber();
    }

    private int id;

    private String name;

//    private int slot;

    private int port;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public int getSlot() {
//        return slot;
//    }
//
//    public void setSlot(int slot) {
//        this.slot = slot;
//    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
