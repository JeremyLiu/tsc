package com.jec.plugin.debug.core;

import com.jec.plugin.debug.Debugger.Level;

public class DebugEntry {

	private long time;
	private Level level;
	private String module;
	private String content;
	
	public DebugEntry(Level level, String module, String content) {
		super();
		this.time = System.currentTimeMillis();
		this.level = level;
		this.module = module;
		this.content = content;
	}
	
	public long getTime() {
		return time;
	}
	public Level getLevel() {
		return level;
	}
	public String getContent() {
		return content;
	}
	public String getModule() {
		return module;
	}
	
	
	
}
