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
        this.code = terminal.getCode();
//        this.slot = terminal.getSlotNumber();
    }

    private int id;

    private String name;

    private String code;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
