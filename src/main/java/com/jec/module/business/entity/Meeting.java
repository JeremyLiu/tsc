package com.jec.module.business.entity;

import com.jec.base.entity.NetState;
import com.jec.protocol.unit.BCD;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Meeting extends NetState implements Serializable{

	private BCD number;
	
	private String name;
	
	private BCD caller;
	
	private List<Member> members = new ArrayList<Member>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number.toString();
	}

	public void setNumber(BCD number) {
		this.number = number;
	}

	public String getCaller() {
		return caller.toString();
	}

	public void setCaller(BCD caller) {
		this.caller = caller;
	}

	public List<Member> getMembers() {
		return members;
	}

	public void setMembers(List<Member> members) {
		this.members = members;
	}

	@Override
	public void from(NetState netState) {
		super.from(netState);
		if (netState instanceof Meeting) {
			Meeting meeting = (Meeting) netState;
			members = meeting.members;
			caller = meeting.caller;
			name = meeting.name;
		}
	}
	@Override
	public boolean equalWith(NetState netState){
		if (netState instanceof Meeting) {
			Meeting meeting = (Meeting) netState;
			return number.equals(meeting.number);
		}
		else
			return false;
	}
}
