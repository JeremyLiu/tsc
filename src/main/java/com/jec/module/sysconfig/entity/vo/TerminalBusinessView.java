package com.jec.module.sysconfig.entity.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by jeremyliu on 09/10/2016.
 */
@Entity
@Table(name="zhwg_view_config_terminal_business")
public class TerminalBusinessView implements Serializable{

    public final static String splitChar = "\\n|\\r\\n| |\\t";

    @Id
    private int id;

    @Column(name="device_number")
    private String deviceNumber;

    @Column(name="device_name")
    private String deviceName;

    private int type;

    @Column(name="element_id")
    private int netunit;

    @Column(name="key_type")
    private int keyType;

    @Column(name="key_value")
    private int keyValue;

    private String code;

    private String name;

    @Column(name="members")
    private String memberStr;


    transient private Member[] members;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeviceNumber() {
        return deviceNumber;
    }

    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getNetunit() {
        return netunit;
    }

    public void setNetunit(int netunit) {
        this.netunit = netunit;
    }

    public int getKeyType() {
        return keyType;
    }

    public void setKeyType(int keyType) {
        this.keyType = keyType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(int keyValue) {
        this.keyValue = keyValue;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getMembers() {
        return memberStr.split(splitChar);
    }

    public void setMemberStr(String memberStr) {
        this.memberStr = memberStr;
    }

    public void setMembers(Member[] members) {
        this.members = members;
    }

    @JsonIgnore
    public Member[] getMemberList(){
        return members;
    }
}
