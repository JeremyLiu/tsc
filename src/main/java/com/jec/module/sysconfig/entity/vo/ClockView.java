package com.jec.module.sysconfig.entity.vo;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by jeremyliu on 9/20/16.
 */
public class ClockView implements Serializable {

    private String netunit;

    private String type1;

    private int slot1;

    private int port1;

    private String type2;

    private int slot2;

    private int port2;

    private String type3;

    private int slot3;

    private int port3;

    private Date updateDate;

    public String getNetunit() {
        return netunit;
    }

    public void setNetunit(String netunit) {
        this.netunit = netunit;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public int getSlot1() {
        return slot1;
    }

    public void setSlot1(int slot1) {
        this.slot1 = slot1;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public int getPort3() {
        return port3;
    }

    public void setPort3(int port3) {
        this.port3 = port3;
    }

    public String getType3() {
        return type3;
    }

    public void setType3(String type3) {
        this.type3 = type3;
    }

    public int getSlot3() {
        return slot3;
    }

    public void setSlot3(int slot3) {
        this.slot3 = slot3;
    }

    public int getPort2() {
        return port2;
    }

    public void setPort2(int port2) {
        this.port2 = port2;
    }

    public int getSlot2() {
        return slot2;
    }

    public void setSlot2(int slot2) {
        this.slot2 = slot2;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public int getPort1() {
        return port1;
    }

    public void setPort1(int port1) {
        this.port1 = port1;
    }
}
