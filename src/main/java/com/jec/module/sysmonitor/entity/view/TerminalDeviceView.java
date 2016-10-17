package com.jec.module.sysmonitor.entity.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jec.module.sysmonitor.entity.DevicePort;
import com.jec.protocol.unit.BCD;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeremyliu on 5/10/16.
 */
@Entity
@Table(name="zhwg_view_terminal_device")
public class TerminalDeviceView implements Serializable{

    @Id
    private int id;

    private String name;

    @Column(name="element_id")
    private int netUnitId;

    @Column(name="element_name")
    private String netUnitName;

    private String code;

    @OneToMany(targetEntity = DevicePort.class, mappedBy = "deviceId")
    private List<DevicePort> ports = new ArrayList<>();

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

    public int getNetUnitId() {
        return netUnitId;
    }

    public void setNetUnitId(int netUnitId) {
        this.netUnitId = netUnitId;
    }

//    public int getSlotNumber() {
//        return slotNumber;
//    }
//
//    public void setSlotNumber(int slotNumber) {
//        this.slotNumber = slotNumber;
//    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNetUnitName() {
        return netUnitName;
    }

    public void setNetUnitName(String netUnitName) {
        this.netUnitName = netUnitName;
    }

    @JsonIgnore
    public List<DevicePort> getPorts() {
        return ports;
    }

    public void setPorts(List<DevicePort> ports) {
        this.ports = ports;
    }

    @JsonIgnore
    public BCD getDeafultPort(){
        return BCD.fromString(String.valueOf(code));
    }
}
