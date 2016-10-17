package com.jec.module.business.entity;
import com.jec.base.entity.NetState;
import com.jec.protocol.unit.BCD;

import java.io.Serializable;

public class Member extends NetState implements Serializable{

	private BCD number;

	public String getNumber() {
		return number.toString();
	}

	public void setNumber(BCD number) {
		this.number = number;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((number == null) ? 0 : number.hashCode());
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
		Member other = (Member) obj;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		return true;
	}
	
	
	
}
