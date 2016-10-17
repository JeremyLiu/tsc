package com.jec.module.sysmanage.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jeremyliu on 6/13/16.
 */
@Entity
@Table(name="zhwg_user_role")
public class Role implements Serializable{

    public final static int ROLE_ADMIN = 1;
    public final static int ROLE_DEV = 2;
    public final static int ROLE_USER = 3;

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment",strategy="increment")
    private int id;

    private String name;

    @Column(name="create_date")
    private Date createTime;

    @OneToMany(targetEntity = Privilege.class,
            mappedBy = "role", fetch=FetchType.EAGER,
            cascade = {CascadeType.MERGE,CascadeType.REMOVE})
    private List<Privilege> privilege;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<Privilege> getPrivilege() {
        return privilege;
    }

    public void setPrivilege(List<Privilege> privilege) {
        this.privilege = privilege;
    }

    public String toValue(){
        if(privilege == null)
            return "";
        String value = "";
        for(Privilege p : privilege)
            value += "," + p.getValue();
        return value.substring(1);
    }
}
