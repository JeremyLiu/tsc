package com.jec.module.business.entity;


import com.jec.base.entity.NetState;
import com.jec.protocol.unit.BCD;
import sun.nio.ch.Net;

import java.io.Serializable;

public class ShoreLine extends NetState implements Serializable{
	
	public static final int TYPE_DR = 0; //数字中继
	public static final int TYPE_SR = 1; //模拟中继
	
	public String getTypeText() {
		switch(type) {
		case TYPE_DR: return "数字中继";
		case TYPE_SR: return "模拟中继";
		default: return "未知类型(" + type + ")";
		}
	}
	
	private int type;
	
	private int slot;
	
	private int ts;

	private BCD number;
	
	private BCD shoreNumber;
	
	private int shoreState;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

	public int getTs() {
		return ts;
	}

	public void setTs(int ts) {
		this.ts = ts;
	}

	public String getNumber() {
		return number.toString();
	}

	public void setNumber(BCD number) {
		this.number = number;
	}

	public String getShoreNumber() {
		return shoreNumber.toString();
	}

	public void setShoreNumber(BCD shoreNumber) {
		this.shoreNumber = shoreNumber;
	}

	public int getShoreState() {
		return shoreState;
	}

	public void setShoreState(int shoreState) {
		this.shoreState = shoreState;
	}

	@Override
	public void from(NetState netState){
		super.from(netState);
		if( netState instanceof ShoreLine){
			ShoreLine shoreLine = (ShoreLine) netState;
			number = shoreLine.number;
		}
	}

	@Override
	public boolean equalWith(NetState netState){
		if( netState instanceof ShoreLine){
			ShoreLine shoreLine = (ShoreLine) netState;
			return shoreLine.slot == slot && shoreLine.ts == ts;
		}else
			return false;
	}
}
