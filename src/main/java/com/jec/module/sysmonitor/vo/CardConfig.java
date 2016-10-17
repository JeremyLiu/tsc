package com.jec.module.sysmonitor.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jec.module.sysmonitor.entity.TerminalDevice;
import com.jec.module.sysmonitor.entity.view.CardView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeremyliu on 5/12/16.
 */
public class CardConfig implements Serializable {

    private int id = 0;

    private int type = 0;

    private String name = "";

    private int portCount = 0;

    private TerminalDevice[] devices = new TerminalDevice[0];

    public CardConfig(){

    }

    public CardConfig(CardView cardView){
        id = cardView.getId();
        type = cardView.getCode();
        name = cardView.getName();
        portCount = cardView.getPortCount();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public TerminalDevice[] getDevices() {
        return devices;
    }

    public void setDevices(TerminalDevice[] devices) {
        this.devices = devices;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPortCount() {
        return portCount;
    }

    public void setPortCount(int portCount) {
        this.portCount = portCount;
    }
}
