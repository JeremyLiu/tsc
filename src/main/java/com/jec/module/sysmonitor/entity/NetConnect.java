package com.jec.module.sysmonitor.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by jeremyliu on 5/20/16.
 */
@Entity
@Table(name="zhwg_element_connect")
@IdClass(NetConnectId.class)
public class NetConnect implements Serializable {

    @Id
    @Column(name="src_id")
    @XStreamAsAttribute
    @XStreamAlias("SourceNEID")
    private int srcId;

    @Id
    @Column(name="dest_id")
    @XStreamAlias("TargetNEID")
    @XStreamAsAttribute
    private int destId;

    @Column(name="dest_port")
    @XStreamOmitField
    private int port;

    @Column(name="dest_slot")
    @XStreamOmitField
    private int slot;

//    @EmbeddedId
//    @JsonIgnore
//    public String getId(){
//        return srcId + "_" + destId;
//    }
//
//    public void setId(String id){
//
//    }

    public int getSrcId() {
        return srcId;
    }

    public void setSrcId(int srcId) {
        this.srcId = srcId;
    }

    public int getDestId() {
        return destId;
    }

    public void setDestId(int destId) {
        this.destId = destId;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }
}
