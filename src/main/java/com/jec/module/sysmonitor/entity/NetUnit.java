package com.jec.module.sysmonitor.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by jeremyliu on 4/11/16.
 */
@Entity
@Table(name="zhwg_monitor_element")
public class NetUnit implements Serializable{

    @Id
    @Column(name="id")
    private int id;

    @Column(name="net_id")
    private int netId;

    private String name;

    private String ip;

    private int port;

    @Column(name="cardCount")
    private int cardCount;

    @Column(name="device_id")
    private int deviceId = 1;

    @Column(name="create_by")
    private int createBy = 1;

    public static int getIdFromIp(String ip){
        return Integer.parseInt(ip.split("\\.")[2]);
    }

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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @JsonIgnore
    public int getNetId() {
        return netId;
    }

    public void setNetId(int netId) {
        this.netId = netId;
    }

    @JsonIgnore
    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    @JsonIgnore
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getCardCount() {
        return cardCount;
    }

    public void setCardCount(int cardCount) {
        this.cardCount = cardCount;
    }

    public int getCreateBy() {
        return createBy;
    }

    public void setCreateBy(int createBy) {
        this.createBy = createBy;
    }
}
