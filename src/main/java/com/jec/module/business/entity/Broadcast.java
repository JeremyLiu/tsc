package com.jec.module.business.entity;

import com.jec.base.entity.NetState;
import com.jec.protocol.unit.BCD;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Broadcast extends NetState implements Serializable{
	
	private int netunit;
	
	private BCD number;
	
	private BCD device;
	
	private String name;
	
	public List<Member> members = new ArrayList<>();

	public int getNetunit() {
		return netunit;
	}

	public void setNetunit(int netunit) {
		this.netunit = netunit;
	}

	public String getNumber() {
		return number.toString();
	}

	public void setNumber(BCD number) {
		this.number = number;
	}

	public String getDevice() {
		return device.toString();
	}

	public void setDevice(BCD device) {
		this.device = device;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Member> getMembers() {
		return members;
	}

	public void setMembers(List<Member> members) {
		this.members = members;
	}

	@Override
	public void from(NetState netState){
		super.from(netState);
		if(netState instanceof Broadcast){
			Broadcast broadcast = (Broadcast) netState;
			number = broadcast.number;
			device = broadcast.device;
			name = broadcast.name;
			members = broadcast.members;
		}
	}

	@Override
	public boolean equalWith(NetState netState){
		if(netState instanceof Broadcast){
			Broadcast broadcast = (Broadcast) netState;
			return number.equals(broadcast.number);
		}else
			return false;
	}
}
