package com.jec.module.sysconfig.entity.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by jeremyliu on 09/10/2016.
 */
@Entity
@Table(name="zhwg_view_config_terminal_key")
public class TerminalKeyConfigView implements Serializable {

    @Id
    private int id;

    @Column(name="device_number")
    private String deviceNumber;

    @Column(name="device_name")
    private String deviceName;

    @Column(name="element_id")
    private int netunit;

    @Column(name="key_type")
    private int keyType;

    @Column(name="key_value")
    private int keyValue;

    @Column(name="business_type")
    private int businessType;

    @Column(name="user_level")
    private int userLevel;

    private String name;

    private String code;

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

    public int getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(int keyValue) {
        this.keyValue = keyValue;
    }

    public int getBusinessType() {
        return businessType;
    }

    public void setBusinessType(int businessType) {
        this.businessType = businessType;
    }

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
