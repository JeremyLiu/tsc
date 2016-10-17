package com.jec.module.business.entity;

import com.jec.base.entity.NetState;
import com.jec.protocol.unit.BCD;

import javax.jws.WebService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WtBusiness extends NetState implements Serializable{
	
	private int netunit;

	private BCD number;
	
	private String name;
	
	private int type;
	
	private BCD chairman;
	
	private List<BCD> listeners = new ArrayList<>();
	
	private List<BCD> devices = new ArrayList<BCD>();

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getChairman() {
		return chairman.toString();
	}

	public void setChairman(BCD chairman) {
		this.chairman = chairman;
	}

	public List<BCD> getListeners() {
		return listeners;
	}

	public void setListeners(List<BCD> listeners) {
		this.listeners = listeners;
	}

	public List<BCD> getDevices() {
		return devices;
	}

	public void setDevices(List<BCD> devices) {
		this.devices = devices;
	}

	@Override
	public void from(NetState netState){
		super.from(netState);
		if(netState instanceof WtBusiness){
			WtBusiness wtBusiness = (WtBusiness) netState;
			type = wtBusiness.type;
			chairman = wtBusiness.chairman;
			name = wtBusiness.name;
			listeners = wtBusiness.listeners;
			devices = wtBusiness.devices;
		}
	}

	@Override
	public boolean equalWith(NetState netState){
		if (netState instanceof Meeting) {
			WtBusiness wtBusiness = (WtBusiness) netState;
			return number.equals(wtBusiness.number);
		}
		else
			return false;
	}
}
