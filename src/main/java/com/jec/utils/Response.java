package com.jec.utils;

import java.io.Serializable;
import java.util.HashMap;

public class Response extends HashMap<String,Object> implements Serializable{
	
	private static final long serialVersionUID = -8094644943551469043L;
	
	private static int code=0;
	
	private static String SUCCESS="";


	public final static int STATUS_SUCCESS = 0;
	public final static int STATUS_PARAM_ERROR = 1;
	public final static int STATUS_SYS_ERROR = 2;

	public static Response Builder(Object entity)
	{
		Response resp=new Response();
		resp.put("status", Response.code);
		resp.put("message", Response.SUCCESS);
		resp.put("data",entity);
		return resp;
	}
	
	public static Response Builder(){
		Response resp=new Response();
		resp.put("status", Response.code);
		resp.put("message", Response.SUCCESS);
		return resp;
	}
	
	public Response status(int status){
		this.put("status", status);
		return this;
	}
	
	public Response message(String message){
		this.put("message", message);
		return this;
	}

	public Response data(Object data){
		this.put("data", data);
		return this;
	}
}
