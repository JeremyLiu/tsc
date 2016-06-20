package com.jec.base.entity;

public class MonitorConstant {

	public static final int BUSINESS_STATE_IDLE = 0;
	
	public static final int BUSINESS_STATE_BUSY = 1;
	
	public static final int BUSINESS_STATE_WT_READY = 2;
	
	public static String BusinessState(int state) {
		switch(state) {
		case BUSINESS_STATE_IDLE: return "空闲";
		case BUSINESS_STATE_BUSY: return "在用";
//		case BUSINESS_STATE_BUSY: return "建链中";
		case BUSINESS_STATE_WT_READY: return "在用";
		default: return "未知";
		}
	}
}
