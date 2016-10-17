package com.jec.module.business.entity;


import com.jec.base.entity.NetState;
import com.jec.protocol.unit.BCD;

import java.io.Serializable;

public class Twotalk extends NetState implements Serializable{

	private BCD caller;
	
	private Member second = new Member();
//
//	public int getNetunit() {
//		return netunit;
//	}
//
//	public void setNetunit(int netunit) {
//		this.netunit = netunit;
//	}

	public String getCaller() {
		return caller.toString();
	}

	public void setCaller(BCD caller) {
		this.caller = caller;
	}

	public Member getSecond() {
		return second;
	}

	public void setSecond(Member second) {
		this.second = second;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((caller == null) ? 0 : caller.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Twotalk other = (Twotalk) obj;
		if (caller == null) {
			if (other.caller != null)
				return false;
		} else if (!caller.equals(other.caller))
			return false;
		if (second == null) {
			if (other.second != null)
				return false;
		} else if (!second.equals(other.second))
			return false;
		return true;
	}
	
	@Override
	public void from(NetState netState){
		super.from(netState);
		if( netState instanceof Twotalk){
			Twotalk twotalk = (Twotalk) netState;
			second = twotalk.second;
		}
	}
	
}
