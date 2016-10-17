package com.jec.module.sysconfig.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jec.utils.Response;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by jeremyliu on 09/10/2016.
 */
@Entity
@Table(name="zhwg_config_terminal_business")
public class TerminalBusiness implements NetUnitConfig, Serializable{

    public final static String splitChar = "\\n|\\r\\n| |\\t";

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment",strategy="increment")
    private int id;

    @Column(name="device_number")
    private String deviceNumber="";

    @Column(name="key_config_id")
    private int keyConfigId;

    private int type;

    private String members="";

    @Column(name="update_date")
    private Date updateDate;

    @Override
    public int getNetunit() {
        return -1;
    }

    @Override
    public Response validate() {
        Response response = Response.Builder().status(Response.STATUS_SUCCESS);
        if(type<1 || type>3)
            return response.message("业务类型不合法");

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

    public int getKeyConfigId() {
        return keyConfigId;
    }

    public void setKeyConfigId(int keyConfigId) {
        this.keyConfigId = keyConfigId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMembers() {
        return members;
    }

    @JsonIgnore
    public String[] getMemberList(){
        if(members.equals(""))
            return new String[0];
        return this.members.split(splitChar);
    }

    public void setMembers(String members) {
        this.members = members;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
