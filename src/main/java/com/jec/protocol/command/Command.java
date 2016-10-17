package com.jec.protocol.command;

import java.util.LinkedList;
import java.util.List;

import com.jec.protocol.pdu.PDU;

public abstract class Command {

	protected boolean result = false;

	protected String errorText = "";
	
	public static interface Verifier {
		public void verify(PDU request, PDU response) throws Exception;
	}
	
	/**
	 * 请求PDU
	 */
	protected PDU request = null;
	
	/**
	 * 回应PDU
	 */
	protected PDU response = null;
	
	/**
	 * 是否需要接受响应
	 */
	protected boolean needResponse = true;
	
	/**
	 * 验证器列表
	 */
	private List<Verifier> verifiers = new LinkedList<Verifier>();
	
	
	
	public Command() {
		super();
	}
	
	
	
	public boolean isNeedResponse() {
		return needResponse;
	}



	public void setNeedResponse(boolean needResponse) {
		this.needResponse = needResponse;
	}



	/**
	 * 
	 * @return
	 */
	void prepareRequest() throws Exception {
		
		request = buildRequestPdu();
		
		if(request == null) {
			throw new RuntimeException("创建了空的请求协议数据单元！");
		}
		
	}
	
	void processResponse(PDU pdu) throws Exception {
		
		response = pdu;
		
		for(Verifier v : verifiers) {
			v.verify(request, response);
		}
		
		processResponsePdu(pdu);
		
	}
	
	/*
	 * 回调函数
	 */
	public abstract PDU buildRequestPdu() throws Exception;
	
	public abstract void processResponsePdu(PDU pdu) throws Exception;

	
	/*
	 * 辅助函数
	 */
	/**
	 * 设置错误信息
	 * @param description
	 */
	protected final void error(String error) throws RuntimeException {
		throw new RuntimeException(getClass().getSimpleName() + ": " + error);
	}
	
	public boolean getResult(){
		return result;
	}

	public String getErrorText() {
		return errorText;
	}
}
