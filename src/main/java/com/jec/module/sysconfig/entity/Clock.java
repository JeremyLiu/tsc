package com.jec.module.sysconfig.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jec.utils.Response;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by jeremyliu on 7/2/16.
 */
@Entity
@Table(name="zhwg_config_clock")
public class Clock implements Serializable, NetUnitConfig {

    @Id
    @Column(name="element_id")
    private int netunit;

    private int type1=-1;

    private int slot1=-1;

    private int port1=-1;

    private int type2=-1;

    private int slot2=-1;

    private int port2=-1;

    private int type3=-1;

    private int slot3=-1;

    private int port3=-1;

    @Column(name="update_date")
    private Date updateDate;

    public int getNetunit() {
        return netunit;
    }

    @Override
    public Response validate() {
        Response resp = Response.Builder().status(Response.STATUS_PARAM_ERROR);
        if(slot1<0 || port1<0)
            return resp.message("主时钟设置不能为空");
        if(slot2<0 || port2<0)
            return resp.message("备时钟1设置不能为空");
        if(slot3<0 || port3<0)
            return resp.message("备时钟2设置不能为空");
        return resp.status(Response.STATUS_SUCCESS);
    }

    @Override
    public void setUpdateDate() {
        updateDate = new Date();
    }

    public void setNetunit(int netunit) {
        this.netunit = netunit;
    }

    public int getType1() {
        return type1;
    }

    public void setType1(int type1) {
        this.type1 = type1;
    }

    public int getSlot1() {
        return slot1;
    }

    public void setSlot1(int slot1) {
        this.slot1 = slot1;
    }

    public int getPort1() {
        return port1;
    }

    public void setPort1(int port1) {
        this.port1 = port1;
    }

    public int getType2() {
        return type2;
    }

    public void setType2(int type2) {
        this.type2 = type2;
    }

    public int getSlot2() {
        return slot2;
    }

    public void setSlot2(int slot2) {
        this.slot2 = slot2;
    }

    public int getPort2() {
        return port2;
    }

    public void setPort2(int port2) {
        this.port2 = port2;
    }

    public int getType3() {
        return type3;
    }

    public void setType3(int type3) {
        this.type3 = type3;
    }

    public int getSlot3() {
        return slot3;
    }

    public void setSlot3(int slot3) {
        this.slot3 = slot3;
    }

    public int getPort3() {
        return port3;
    }

    public void setPort3(int port3) {
        this.port3 = port3;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
