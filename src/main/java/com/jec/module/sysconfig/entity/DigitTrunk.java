package com.jec.module.sysconfig.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jec.utils.Response;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by jeremyliu on 9/25/16.
 */
@Entity
@Table(name="zhwg_config_digittrunk")
public class DigitTrunk implements NetUnitConfig, Serializable{

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment",strategy="increment")
    private int id;

    private int netunit;

    private int slot = -1;

    private int port = -1;

    private String opc;

    private String dpc;

    private String cic;

    @Column(name="update_date")
    private Date updateDate;

    @Override
    public int getNetunit() {
        return netunit;
    }

    @Override
    public Response validate() {
        Response res = Response.Builder().status(Response.STATUS_PARAM_ERROR);
        if(netunit<=0)
            return res.message("网元不能为空");
        if(slot<0)
            return res.message("槽位不能为空");
        if(port<0)
            return res.message("端口不能为空");
        if(!digitTrunkValueValidate(opc))
            return res.message("opc值不合法");
        if(!digitTrunkValueValidate(dpc))
            return res.message("dpc值不合法");
        if(!digitTrunkValueValidate(cic))
            return res.message("cic值不合法");
        return res.status(Response.STATUS_SUCCESS);
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

    public void setNetunit(int netunit) {
        this.netunit = netunit;
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

    public String getOpc() {
        return opc;
    }

    public void setOpc(String opc) {
        this.opc = opc;
    }

    public String getDpc() {
        return dpc;
    }

    public void setDpc(String dpc) {
        this.dpc = dpc;
    }

    public String getCic() {
        return cic;
    }

    public void setCic(String cic) {
        this.cic = cic;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public static boolean digitTrunkValueValidate(String value){
        if(value==null || value.equals(""))
            return false;
        String[] values = value.split("\\.");
        if(values.length != 3)
            return false;
        try {
            for (String str : values)
                Integer.parseInt(str);
        }catch (NumberFormatException e){
            return false;
        }
        return true;
    }
}
