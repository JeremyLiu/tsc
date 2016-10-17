package com.jec.module.sysconfig.entity;

import com.jec.protocol.unit.BCD;
import com.jec.utils.Response;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by jeremyliu on 08/10/2016.
 */
@Entity
@Table(name="zhwg_view_config_vdr")
public class VdrConfig implements NetUnitConfig, Serializable {
    @Id
    private int id;

    private int netunit;

    private boolean enable;

    private String number;

    @Column(name="device_name")
    private String deviceName;

    @Column(name="port_number")
    private String portNumber;

    @Override
    public int getNetunit() {
        return netunit;
    }

    @Override
    public Response validate() {
        Response response = Response.Builder().status(Response.STATUS_PARAM_ERROR);
        if(number.equals(""))
            return response.message("终端号码不能为空");
        if(BCD.fromString(number) == null)
            return response.message("终端号码格式错误");

        if(portNumber.equals(""))
            return response.message("终端端口号码不能为空");
        if(BCD.fromString(portNumber) == null)
            return response.message("终端端口号码格式错误");
        return response.status(Response.STATUS_SUCCESS);
    }

    @Override
    public void setUpdateDate() {
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPortNumber() {
        return portNumber;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }
}
