package com.jec.module.sysmonitor.entity.view;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jec.module.sysmonitor.entity.TerminalDevice;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by jeremyliu on 5/10/16.
 */
@Entity
@Table(name="zhwg_view_card")
public class CardView implements Serializable{

    @Id
    private int id;

    @Column(name="slot_number")
    private int slot;

    @Column(name="port_count")
    private int portCount;

    private String name;

    @Column(name="element_id")
    private int netUnitId;

    private int code;

//    @OneToMany(targetEntity = TerminalDevice.class, mappedBy = "cardId")
//    private List<TerminalDevice> device;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public int getPortCount() {
        return portCount;
    }

    public void setPortCount(int portCount) {
        this.portCount = portCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public int getNetUnitId() {
        return netUnitId;
    }

    public void setNetUnitId(int netUnitId) {
        this.netUnitId = netUnitId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

//    public List<TerminalDevice> getDevice() {
//        return device;
//    }
//
//    public void setDevice(List<TerminalDevice> device) {
//        this.device = device;
//    }
}
