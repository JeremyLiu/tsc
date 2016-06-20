package com.jec.module.sysmanage.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name="zhwg_user")
public class User implements Serializable {

	protected static final long serialVersionUID = -2264997227937470104L;
	
	@Id
	@Column(name="id")
	@GeneratedValue(generator="increment")
	@GenericGenerator(name="increment",strategy="increment")
	private int id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="password")
	private String password;
	
	@Column(name="create_date")
	private Date createTime;

	@Column(name="role_id")
	private int roleId;

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id=id;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name=name;
	}

	@JsonIgnore
	public String getPassword(){
		return this.password;
	}
	
	public void setPassword(String password){
		this.password=password;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime(){
		return this.createTime;
	}
	
	public void setCreateTime(){
		this.createTime=new Date();
	}
	
}
