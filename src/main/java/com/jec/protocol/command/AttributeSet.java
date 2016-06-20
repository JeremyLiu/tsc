package com.jec.protocol.command;

import java.util.HashMap;
import java.util.Map;

public class AttributeSet {
	
	private Map<String, Object> map = new HashMap<String, Object>();

	public AttributeSet() {
	}

	public Object set(String key, Object value) {
		return map.put(key, value);
	}
	
	public Object get(String key) {
		return map.get(key);
	}
	
	public Result get(String key, Class<?> clazz) {
		
		Object obj  = map.get(key);
		
		if(obj == null) {
			return Result.newFailure(key + " is null.", null);
		}

		if(!obj.getClass().equals(clazz)) {
			
			return Result.newFailure(key + " is wrong type.", null);
			
		} 
			
		return Result.newSuccess(obj);
		
	}
	
}
