package com.jec.module.sysmanage.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by jeremyliu on 6/16/16.
 */
@Entity
@Table(name="zhwg_user_privilege")
public class Privilege implements Serializable {
    @Id
    @Column(name="id")
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment",strategy="increment")
    @JsonIgnore
    private int id;

    @Column(name="resource_id")
    private String value;

    @JsonIgnore
    @Column(name="role_id")
    private int role;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
