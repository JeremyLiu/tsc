package com.jec.module.sysmonitor.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by jeremyliu on 5/19/16.
 */
@Entity
@Table(name="zhwg_device_port")
public class DevicePort implements Serializable{

    @Id
    private int id;

    @Column(name="device_id")
    private int deviceId;

    private String function;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonIgnore
    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }
}
