package com.jec.module.sysconfig.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.jec.utils.Response;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jeremyliu on 7/2/16.
 */
@Entity
@Table(name="zhwg_config_tongling")
public class TonglingConfig implements Serializable, NetUnitConfig{

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment",strategy="increment")
    private int id;

    public final static String splitChar = "\\n|\\r\\n| |\\t";

    @Column(name="element_id")
    private int netunit;

    private String name;

    private String code;

    private String users = "";

    private String commanders = "";

    private String members = "";

    @Column(name="update_date")
    private Date updateDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNetunit() {
        return netunit;
    }

    @Override
    public Response validate() {
        Response resp = Response.Builder().status(Response.STATUS_PARAM_ERROR);
        if(name==null || name.equals(""))
            return resp.message("名称不能为空");
        if(code==null || code.equals(""))
            return resp.message("编码不能为空");
        if(users==null || users.equals(""))
            return resp.message("用户不能为空");
        if(members==null || members.equals(""))
            return resp.message("成员不能为空");
        String[] userList = getUsers();
        if(!bcdListValidate(userList))
            return resp.message("有权用户格式错误");
        if(userList.length>10)
            return resp.message("有权用户个数不能超过10个");

        String[] memberList = getMembers();
        if(!bcdListValidate(memberList))
            return resp.message("成员格式错误");
        if(memberList.length>40)
            return resp.message("成员个数不能超过40个");

        String[] commanderList = getCommanders();
        if(!bcdListValidate(commanderList))
            return resp.message("指挥格式错误");
        if(commanderList.length>10)
            return resp.message("指挥个数不能超过10个");
        return resp.status(Response.STATUS_SUCCESS);
    }

    @Override
    public void setUpdateDate() {
        updateDate = new Date();
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

    public String[] getUsers() {
        return users.split(splitChar);
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public String[] getCommanders() {
        if(commanders.equals(""))
            return new String[0];
        return commanders.split(splitChar);
    }

    public void setCommanders(String commanders) {
        this.commanders = commanders;
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
