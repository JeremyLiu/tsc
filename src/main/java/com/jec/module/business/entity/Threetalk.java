package com.jec.module.business.entity;


import com.jec.base.entity.NetState;
import com.jec.protocol.unit.BCD;

import java.io.Serializable;

public class Threetalk extends NetState implements Serializable{
	
	private int netunit;

	private BCD caller = new BCD();
	
	private Member second = new Member();
	
	private Member third = new Member();


	public int getNetunit() {
		return netunit;
	}

	public void setNetunit(int netunit) {
		this.netunit = netunit;
	}

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

	public Member getThird() {
		return third;
	}

	public void setThird(Member third) {
		this.third = third;
	}

	@Override
	public void from(NetState netState){
		super.from(netState);
		if( netState instanceof Threetalk){
			Threetalk threetalk = (Threetalk) netState;
			second = threetalk.second;
			third = threetalk.third;
		}
	}

	@Override
	public boolean equalWith(NetState netState){
		if(netState instanceof Threetalk){
			Threetalk threetalk = (Threetalk) netState;
			return threetalk.caller.equals(caller);
		}else
			return false;
	}
}
