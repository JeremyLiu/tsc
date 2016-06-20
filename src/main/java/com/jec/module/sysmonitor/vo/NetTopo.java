package com.jec.module.sysmonitor.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jeremyliu on 5/10/16.
 */
public class NetTopo implements Serializable {

    private int id;

    private String name;

    private List<Device> devices;

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

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }
}
