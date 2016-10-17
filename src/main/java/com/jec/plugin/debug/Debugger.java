package com.jec.plugin.debug;

public interface Debugger {
	
	public static enum Level {
		FATAL,		// 致命 
		ERROR, 		// 错误
		WARNING, 	// 警告
		MESSAGE, 	// 消息
		DEBUG;		// 调试
	}
	
	public void log(Level level, String module, String message);
	
	/*
	public void fatal(String message);
	
	public void error(String message);
	
	public void warn(String message);
	
	public void message(String message);
	
	public void debug(String message);
	*/
	
}
