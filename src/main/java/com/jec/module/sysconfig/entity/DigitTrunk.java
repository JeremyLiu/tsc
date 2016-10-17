package com.jec.module.sysconfig.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    private int mode;

    private int interfaceType;

    private int distanceMode;

    private int clockMode;

    private String opc;

    private String dpc;

    @Column(name="pcmnum")
    private int pcmNum;

    @Column(name="pcmts")
    private int pcmTs;

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

        if(mode<1 || mode>3)
            return res.message("模式格式错误");
        if(port==0 && mode==1)
            return res.message("端口为0 不能设置为直通模式");
        if(interfaceType<0 || interfaceType>1)
            return res.message("接口类型错误");
        if(distanceMode<0 || interfaceType>1)
            return res.message("距离模式格式错误");
        if(clockMode<0 || clockMode>1)
            return res.message("时钟模式错误");
        if(digitTrunkValue(opc)<0)
            return res.message("opc值不合法");
        if(digitTrunkValue(dpc)<0)
            return res.message("dpc值不合法");
        if(pcmTs>31 || pcmTs<0)
            return res.message("PCM时隙号格式错误");
        if(pcmNum<0 || pcmTs>127)
            return res.message("PCM序号格式错误");
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

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(int interfaceType) {
        this.interfaceType = interfaceType;
    }

    public int getDistanceMode() {
        return distanceMode;
    }

    public void setDistanceMode(int distanceMode) {
        this.distanceMode = distanceMode;
    }

    public int getClockMode() {
        return clockMode;
    }

    public void setClockMode(int clockMode) {
        this.clockMode = clockMode;
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

    @JsonIgnore
    public int getOpcValue(){
        return digitTrunkValue(opc);
    }

    @JsonIgnore
    public int getDpcValue(){
        return digitTrunkValue(dpc);
    }

    @JsonIgnore
    public int getCic() {
        return (pcmNum << 5) | pcmTs;
    }

    public int getPcmNum() {
        return pcmNum;
    }

    public void setPcmNum(int pcmNum) {
        this.pcmNum = pcmNum;
    }

    public int getPcmTs() {
        return pcmTs;
    }

    public void setPcmTs(int pcmTs) {
        this.pcmTs = pcmTs;
    }


    public Date getUpdateDate() {
        return updateDate;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    private static int digitTrunkValue(String value){
        if(value==null || value.equals(""))
            return -1;
        String[] values = value.split("\\.");
        if(values.length != 3)
            return -1;
        int result=0;
        try {
            for (int i=0;i<values.length;i++) {
                int v = Integer.parseInt(values[i]);
                if(v>255 || v<0)
                    return -1;
                result = result | (v<< (i*8));
            }
        }catch (NumberFormatException e){
            return -1;
        }
        return result;
    }
}
