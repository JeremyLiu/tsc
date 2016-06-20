package com.jec.protocol.command;

import java.io.Serializable;

public class Result implements Serializable{
	
	private String description = "";
	private int code = 0;
	private boolean succeed = false;
	private Object value = null;

	public Result(String description, int code, boolean succeed, Object value) {
		super();
		this.description = description;
		this.code = code;
		this.succeed = succeed;
		this.value = value;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCode() {
		return code;
	}
	
	public Object getValue() {
		return value;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getValue(Class<T> clazz) {
		if(value == null) {
			return null;
		}
		if(value.getClass().equals(clazz)) {
			return (T)value;
		}
		return null;
	}

	public boolean isSucceed() {
		return succeed;
	}

	/*
	 * Help creator
	 */
	
	public static Result newSuccess(String description, int code) {
		return new Result(description, code, true, null);
	}
	
	public static Result newSuccess(String description) {
		return new Result(description, 0, true, null);
	}
	
	public static Result newSuccess() {
		return new Result("", 0, true, null);
	}
	
	public static Result newFailure(String description, int code) {
		return new Result(description, code, false, null);
	}
	
	public static Result newFailure(String description) {
		return new Result(description, 0, false, null);
	}
	
	public static Result newFailure() {
		return new Result("", 0, false, null);
	}
	
	
	/*
	 * help creator
	 */
	public static Result newSuccess(String description, int code, Object value) {
		return new Result(description, code, true, value);
	}
	
	public static Result newSuccess(String description, Object value) {
		return new Result(description, 0, true, value);
	}
	
	public static Result newSuccess(Object value) {
		return new Result("", 0, true, value);
	}
	
	public static Result newFailure(String description, int code, Object value) {
		return new Result(description, code, false, value);
	}
	
	public static Result newFailure(String description, Object value) {
		return new Result(description, 0, false, value);
	}
	
	public static Result newFailure(Object value) {
		return new Result("", 0, false, value);
	}

	@Override
	public String toString(){
		return "code:"+code+";"+
				"msg:"+description+";"+
				"success:"+ succeed+";"+
				"value:"+ value;
	}
}
