package com.jec.module.business.entity;

import com.jec.base.entity.NetState;
import com.jec.protocol.unit.BCD;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tongling extends NetState implements Serializable{
	
	private int netunit;

	private BCD number;
	
	private String name;
	
	private BCD chairman;
	
	private int nested;
	
	private BCD superior;
	
	private List<Member> commanders = new ArrayList<>();
	
	private List<Member> members = new ArrayList<Member>();

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

	public String getChairman() {
		return chairman.toString();
	}

	public void setChairman(BCD chairman) {
		this.chairman = chairman;
	}

	public int getNested() {
		return nested;
	}

	public void setNested(int nested) {
		this.nested = nested;
	}

	public String getSuperior() {
		return superior.toString();
	}

	public void setSuperior(BCD superior) {
		this.superior = superior;
	}

	public List<Member> getCommanders() {
		return commanders;
	}

	public void setCommanders(List<Member> commanders) {
		this.commanders = commanders;
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
		if( netState instanceof Tongling){
			Tongling tongling = (Tongling) netState;
			chairman = tongling.chairman;
			members = tongling.members;
			name = tongling.name;
			nested = tongling.nested;
			commanders = tongling.commanders;
			superior = tongling.superior;
		}
	}

	@Override
	public boolean equalWith(NetState netState){
		if( netState instanceof Tongling){
			Tongling tongling = (Tongling) netState;
			return tongling.number.equals(number);
		}else
			return false;
	}
}
