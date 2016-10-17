package com.jec.module.sysmonitor.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by jeremyliu on 4/11/16.
 */
@Entity
@Table(name="zhwg_device_card")
public class Card implements Serializable{

    @Id
    @Column(name="id")
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment",strategy="increment")
    private int id;

    private int type;

    @Column(name="slot_number", updatable = false)
    private int slotNumber;

    @Column(name="element_id", updatable = false)
    private int netUnitId;

    @Column(name="create_date")
    private Date createDate = new Date();


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSlotNumber() {
        return slotNumber;
    }

    public void setSlotNumber(int slotNumber) {
        this.slotNumber = slotNumber;
    }

    public int getNetUnitId() {
        return netUnitId;
    }

    public void setNetUnitId(int netUnitId) {
        this.netUnitId = netUnitId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

}
