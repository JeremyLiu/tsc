package com.jec.module.sysconfig.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jec.protocol.unit.BCD;
import com.jec.utils.Response;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by jeremyliu on 05/10/2016.
 */
@Entity
@Table(name="zhwg_config_broadcast")
public class BroadcastConfig implements NetUnitConfig, Serializable {

    public final static String splitChar = "\\n|\\r\\n| |\\t";

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy="increment")
    private int id;

    private int netunit;

    private String name = "";

    private String code = "";

    private String broadcaster = "";

    private String users = "";

    private String members= "";

    @Column(name="update_date")
    private Date updateDate;


    @Override
    public int getNetunit() {
        return netunit;
    }

    @Override
    public Response validate() {
        Response response = Response.Builder().status(Response.STATUS_PARAM_ERROR);
        if(netunit<=0)
            return response.message("网元不合法");
        if(name.equals(""))
            return response.message("业务名不能为空");
        if(name.length()>20)
            return response.message("业务名长度超过20个字符");
        if(code.equals(""))
            return response.message("业务代码不能为空");
        if(users==null || users.equals(""))
            return response.message("有权用户不能为空");
        if(members==null || members.equals(""))
            return response.message("成员不能为空");

        if(BCD.fromString(code) == null)
            return response.message("业务代码格式错误");
        if(broadcaster.equals(""))
            return response.message("广播机号码不能为空");
        if(BCD.fromString(broadcaster) == null)
            return response.message("广播机号码格式错误");
        String[] userList = getUsers();
        if(!bcdListValidate(userList))
            return response.message("有权用户格式错误");
        if(userList.length>10)
            return response.message("有权用户个数不能超过10个");

        String[] memberList = getMembers();
        if(!bcdListValidate(memberList))
            return response.message("成员格式错误");
        if(memberList.length>40)
            return response.message("成员个数不能超过40个");
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

    public String getBroadcaster() {
        return broadcaster;
    }

    public void setBroadcaster(String broadcaster) {
        this.broadcaster = broadcaster;
    }

    public String[] getUsers() {
        return users.split(splitChar);
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public String[] getMembers() {
        return members.split(splitChar);
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

    public static boolean bcdListValidate(String[] values){
        try {
            for (String value: values)
                Integer.parseInt(value);
        }catch(NumberFormatException e){
            return false;
        }
        return true;
    }
}
