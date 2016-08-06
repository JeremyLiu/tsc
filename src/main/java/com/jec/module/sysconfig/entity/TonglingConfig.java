package com.jec.module.sysconfig.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jeremyliu on 7/2/16.
 */
public class TonglingConfig implements Serializable{

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment",strategy="increment")
    private int id;

    public final static String splitChar = ",";

    @Column(name="element_id")
    private int netunit;

    private String name;

    private String code;

    private String users = "";

    private String commanders = "";

    private String members = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNetunit() {
        return netunit;
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
}
