package com.jec.module.sysmonitor.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Created by jeremyliu on 5/9/16.
 */
@Entity
@Table(name="zhwg_card_type")
public class CardType implements Serializable {

    @Id
    private int code;

    private String name;

    @Column(name="port_count")
    private int portCount;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPortCount() {
        return portCount;
    }

    public void setPortCount(int portCount) {
        this.portCount = portCount;
    }
}
