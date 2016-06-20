package com.jec.module.business.entity;


import com.jec.base.entity.NetState;
import com.jec.module.sysmonitor.vo.State;
import com.jec.protocol.unit.BCD;

import java.io.Serializable;

public class Vdr extends NetState implements Serializable{
	
	private int netunit;

	private BCD vdr;
	
	private Member user = new Member();

	public int getNetunit() {
		return netunit;
	}

	public void setNetunit(int netunit) {
		this.netunit = netunit;
	}

	public String getVdr() {
		return vdr.toString();
	}

	public void setVdr(BCD vdr) {
		this.vdr = vdr;
	}

	public Member getUser() {
		return user;
	}

	public void setUser(Member user) {
		this.user = user;
	}

	@Override
	public void from(NetState netState){
		super.from(netState);
		if(netState instanceof Vdr){
			Vdr vdr = (Vdr) netState;
			user = vdr.user;
		}
	}

	@Override
	public boolean equalWith(NetState netState){
		if( netState instanceof Vdr){
			Vdr vdr = (Vdr) netState;
			return vdr.vdr.equals(this.vdr);
		}else
			return false;
	}
}
