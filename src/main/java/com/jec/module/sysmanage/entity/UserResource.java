package com.jec.module.sysmanage.entity;

import org.hibernate.annotations.Cache;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by jeremyliu on 07/10/2016.
 */
@Entity
@Table(name="zhwg_user_resource")
public class UserResource implements Serializable {

    @Id
    private String id;

    private String name;

    private String urls;

    private boolean log;

    @Column(name="create_date")
    private Date createDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getUrlList(){
        return urls.split(",");
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public boolean isLog() {
        return log;
    }

    public void setLog(boolean log) {
        this.log = log;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
