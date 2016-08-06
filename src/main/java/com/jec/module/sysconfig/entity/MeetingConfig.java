package com.jec.module.sysconfig.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by jeremyliu on 7/2/16.
 */
@Entity
@Table(name="zhwg_config_meeting")
public class MeetingConfig implements Serializable {

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

    private String members = "";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static String getSplitChar() {
        return splitChar;
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

    public String[] getMembers() {
        return members.split(splitChar);
    }

    public void setMembers(String members) {
        this.members = members;
    }
}
