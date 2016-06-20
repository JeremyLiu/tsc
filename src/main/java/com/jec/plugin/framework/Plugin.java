package com.jec.plugin.framework;

public interface Plugin {

	/*
	 * 模块基本信息
	 */
	public String getName();
	public String getVersion();
	public String getAuthor();
	public String getDescription();
	
	/**
	 * 启动
	 */
	public void start() throws Exception;
	
	/**
	 * 终止
	 */
	public void stop();
	
}
