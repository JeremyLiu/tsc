package com.jec.protocol.pdu;

public class PduConstants {
	/*
	 * 默认网络端口
	 */
	public static final int DEFAULT_NET_PORT = 3600;
	
	/*
	 * 默认长度
	 */
	public static final int LENGTH_OF_STR = 20;		//字符串长度
	public static final int LENGTH_OF_BCD = 9;		//BCD码长度
	public static final int LENGTH_OF_HEAD = 12;	//协议数据格式头长度
	
	/** 通信服务器站号 */
	public static final int ID_LOCAL = 254;
	
	/** 协议类型 */
	public static final int PROTOCOL_TYPE = 0x01;
	
	/*
	 * 板卡类型定义
	 */
	
	public static final int CARD_TYPE_NUL = (byte)0x00; //无板卡
	public static final int CARD_TYPE_MCB = (byte)0x01; //主控板
	public static final int CARD_TYPE_DUB = (byte)0x02; //数字用户板
	public static final int CARD_TYPE_SUB = (byte)0x03; //模拟用户板
	//public static final int CARD_TYPE_ACB = (byte)0x04; //接入控制板
	public static final int CARD_TYPE_ENB = (byte)0x05; //以太网板
	public static final int CARD_TYPE_DRB = (byte)0x04; //数字中继板
	public static final int CARD_TYPE_SRB = (byte)0x08; //录音板
	public static final int CARD_TYPE_OTB = (byte)0x0A; //光传输板
	
	public static final int CARD_TYPE_ZHB = (byte)0x0C; //综合接口板
	
	/*
	 * 命令类型
	 */
	public static final int CMD_TYPE_SBJC = (byte)0x01; //设备检测
	public static final int CMD_TYPE_SBPZ = (byte)0x02; //设备配置
	public static final int CMD_TYPE_YWPZ = (byte)0x03; //业务配置 
	public static final int CMD_TYPE_SBKZ = (byte)0x04; //设备控制
	public static final int CMD_TYPE_YWKZ = (byte)0x05; //业务控制
	public static final int CMD_TYPE_SBJS = (byte)0x06; //设备监视
	public static final int CMD_TYPE_YWJS = (byte)0x07; //业务监视
	public static final int CMD_TYPE_WJXZ = (byte)0x08; //文件下载
	public static final int CMD_TYPE_WJSZ = (byte)0x09; //文件上载
	
	public static final int CMD_TYPE_REPORT_ADDR = (byte)0x0a; //上报地址

	/*
		配置类型
	 */
	public static final int CONFIG_TYPE_DIGITTRUNK = (byte)0x08;
	public static final int CONFIG_TYPE_MEETING = (byte)0x04;

	/*
		响应报文配置参数
	 */
	public static final int MONITOR_NET_CARD = (byte)0x01;
	public static final int MONITOR_NET_PORT = (byte)0x02;
	public static final int MONITOR_NET_DEVICE = (byte)0x04;
	public static final int MONITOR_BUSINESS_USERSTATE = (byte)0x01;
	public static final int MONITOR_BUSINESS_TONGLING = (byte)0x02;
	public static final int MONITOR_BUSINESS_MEETING = (byte)0x03;
	public static final int MONITOR_BUSINESS_THREETALK = (byte)0x04;
	public static final int MONITOR_BUSINESS_SIMULATETRUNK = (byte)0x05;
	public static final int MONITOR_BUSINESS_DIGITALTRUNK = (byte)0x06;
	public static final int MONITOR_BUSINESS_BROADCAST = (byte)0x07;
	public static final int MONITOR_BUSINESS_DIGITALRECORD = (byte)0x08;
	public static final int MONITOR_BUSINESS_VDRRECORD = (byte)0x09;
	public static final int MONITOR_BUSINESS_P2PTALK = (byte)0x0a;

	/*
	 * 命令字
	 */
	public static final int CMD_CODE_SZCS = (byte)0x01; //设置参数
	public static final int CMD_CODE_SZCG = (byte)0x02; //设置成功
	public static final int CMD_CODE_SZSB = (byte)0x03; //设置失败
	public static final int CMD_CODE_SCCS = (byte)0x04; //删除参数
	public static final int CMD_CODE_SCCG = (byte)0x05; //删除成功
	public static final int CMD_CODE_SCSB = (byte)0x06; //删除失败
	public static final int CMD_CODE_PZCX = (byte)0x07; //配置查询
	public static final int CMD_CODE_CXJG = (byte)0x08; //查询结果
	public static final int CMD_CODE_CXSB = (byte)0x09; //查询失败
	public static final int CMD_CODE_ZSCX = (byte)0x0A; //总数查询
	public static final int CMD_CODE_ZSFH = (byte)0x0B; //总数返回
	
	public static final int CMD_CODE_CLEAR = 		(byte)0x0C; //清除所有请求
	public static final int CMD_CODE_CLEAR_ACK = 	(byte)0x0D; //清除所有成功
	public static final int CMD_CODE_CLEAR_FAIL = 	(byte)0x0E; //清除所有失败
	
	public static final int CMD_CODE_REPORT_ADDR_REG = 		(byte)0x01; //上报地址注册
	public static final int CMD_CODE_REPORT_ADDR_REG_ACK = 	(byte)0x02; //上报地址注册成功
	public static final int CMD_CODE_REPORT_ADDR_REG_NACK = (byte)0x03; //上报地址注册失败
	
	public static final int CMD_CODE_CALL = 		(byte)0x01; //呼叫业务
	public static final int CMD_CODE_CALL_ACK = 	(byte)0x02; //呼叫业务回应
	
	public static final int CMD_CODE_HANGUP = 		(byte)0x01; //拆链
	public static final int CMD_CODE_HANGUP_ACK = 	(byte)0x02; //拆链回应

	
	public PduConstants() {
	}

}
