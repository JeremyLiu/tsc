package com.jec.module.sysmonitor.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jeremyliu on 5/10/16.
 */
@Entity
@Table(name="zhwg_terminal_device")
public class TerminalDevice implements Serializable{

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment",strategy="increment")
    private int id;

    @Column(name="element_id")
    private int netUnitId;

    private String name;

    @Column(name="code")
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

    public int getNetUnitId() {
        return netUnitId;
    }

    public void setNetUnitId(int netUnitId) {
        this.netUnitId = netUnitId;
    }
//    public List<DevicePort> getPorts() {
//        return ports;
//    }

//    public void setPorts(List<DevicePort> ports) {
//        this.ports = ports;
//    }

//    @JsonIgnore
//    public int getDeafultPort(){
//        if(ports != null && ports.size()>0)
//            return ports.get(0).getPort();
//        else
//            return 0;
//    }
}
