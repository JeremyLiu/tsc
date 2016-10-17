package com.jec.plugin.debug.utils;

import com.jec.plugin.debug.Debugger;
import com.jec.plugin.framework.Framework;

public class DebugAgent {

	Debugger debugger = null;
	
	String module = "";

	public DebugAgent(String module) {
		super();
		this.module = module;
	}
	
	boolean getDebugger() {
		if(debugger == null) {
			debugger = (Debugger)Framework.getService(Debugger.class);
		}
		return debugger != null;
	}
	
	public void fatal(String message){
		if(getDebugger()) {
			debugger.log(Debugger.Level.FATAL, module, message);
		}
	}
	
	public void error(String message){
		if(getDebugger()) {
			debugger.log(Debugger.Level.ERROR, module, message);
		}
	}
	
	public void warn(String message){
		if(getDebugger()) {
			debugger.log(Debugger.Level.WARNING, module, message);
		}
	}
	
	public void message(String message){
		if(getDebugger()) {
			debugger.log(Debugger.Level.MESSAGE, module, message);
		}
	}
	
	public void debug(String message){
		if(getDebugger()) {
			debugger.log(Debugger.Level.DEBUG, module, message);
		}
	}
	
}
