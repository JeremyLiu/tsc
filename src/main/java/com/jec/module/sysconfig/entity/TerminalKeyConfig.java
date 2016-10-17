package com.jec.module.sysconfig.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jec.protocol.unit.BCD;
import com.jec.utils.Response;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by jeremyliu on 09/10/2016.
 */
@Entity
@Table(name="zhwg_config_terminal_key")
public class TerminalKeyConfig implements NetUnitConfig, Serializable{

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment",strategy="increment")
    private int id;

    @Column(name="device_number")
    private String deviceNumber;

    @Column(name="key_value")
    private int keyValue;

    @Column(name="key_type")
    private int keyType;

    @Column(name="business_type")
    private int businessType;

    private String name;

    private String code;

    @Column(name="user_level")
    private int userLevel;

    @Column(name="update_date")
    private Date updateDate;

    @Override
    public int getNetunit() {
        return -1;
    }

    @Override
    public Response validate() {

        Response response = Response.Builder().status(Response.STATUS_PARAM_ERROR);
        if(keyType<0 || keyType>6)
            return response.message("按键类型非法");
        if(keyValue<0 || keyValue>255)
            return response.message("按键值非法");
        if(businessType<1 || businessType>10)
            return response.message("业务类型非法");
        if(name.equals(""))
            return  response.message("业务名称不能为空");
        if(name.length()>20)
            return response.message("业务名称不能超过20个字符");
        if(code.equals(""))
            return response.message("业务号码不能为空");
        if(userLevel<1 || userLevel>13)
            return response.message("用户级别不合法");
        if(BCD.fromString(code) == null)
            return response.message("业务号码格式错误");

        return response.status(Response.STATUS_SUCCESS);
    }

    @Override
    public void setUpdateDate() {
        updateDate = new Date();
    }

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

    public int getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(int keyValue) {
        this.keyValue = keyValue;
    }

    public int getKeyType() {
        return keyType;
    }

    public void setKeyType(int keyType) {
        this.keyType = keyType;
    }

    public int getBusinessType() {
        return businessType;
    }

    public void setBusinessType(int businessType) {
        this.businessType = businessType;
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

    public int getUserLevel() {
        return userLevel;
    }

    public void setUserLevel(int userLevel) {
        this.userLevel = userLevel;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
