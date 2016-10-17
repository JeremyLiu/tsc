package com.jec.plugin.debug.core;

import java.util.LinkedList;
import java.util.List;

import com.jec.plugin.debug.Debugger;
import com.jec.plugin.debug.core.processors.FileProcessor;
import com.jec.plugin.debug.shell.ShowUI;
import com.jec.plugin.debug.view.DebugViewer;
import com.jec.plugin.framework.Framework;
import com.jec.plugin.framework.Plugin;
import com.jec.plugin.shell.ShellModule;
import com.jec.plugin.shell.ShellService;

public class DebugPlugin implements Plugin, Runnable, Debugger {
	
	private List<DebugEntry> entries = new LinkedList<DebugEntry>();
	
	private List<DebugProcessor> processors = new LinkedList<DebugProcessor>();
	
	ShellModule shellModule = null; 
	
	private DebugViewer debugUI = null;

	@Override
	public String getAuthor() {
		return "lingdm";
	}

	@Override
	public String getDescription() {
		return "提供调试信息输出服务";
	}

	@Override
	public String getName() {
		return "DebugPlugin";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public void start() throws Exception {
		
		//
		debugUI = new DebugViewer();
		processors.add(debugUI);
		processors.add(new FileProcessor());
		
		
		//
		ShellService shell = (ShellService)Framework.getService(ShellService.class);
		if(shell != null) {

			shellModule = shell.createModule("debug", "Debug information output control");
			if(shellModule != null) {
				shellModule.addCommand(new ShowUI(debugUI));
			}
			
		}
		

		// start thread
		Thread thread = new Thread(this);
		thread.setName("DebugPlugin");
		thread.setDaemon(true);
		thread.start();
		
		// register extern
		Framework.registerService(Debugger.class, this);
	}

	@Override
	public void stop() {
		
	}
	
	

	@Override
	public void log(Level level, String module, String message) {
		
		if(module == null || module.isEmpty()) {
			return;
		}
		
		if(message == null || message.isEmpty()) {
			return;
		}
		
		DebugEntry entry = new DebugEntry(level, module, message);
		
		synchronized(entries) {
			
			entries.add(entry);
			try {
				
				entries.notify();
				
			} catch (IllegalMonitorStateException e) {
				
				e.printStackTrace();
				
			}
		}
		
	}

	@Override
	public void run() {
		
		while(true) {
			
			DebugEntry entry = null;
			
			synchronized(entries) {
				
				if(entries.isEmpty()) {
					
					try{
						
						entries.wait();
						
					} catch (InterruptedException e) {
						
						e.printStackTrace();
						
					}
					
				} else {
					
					entry = entries.remove(0);
					
				}
				
				if(entry == null) {
					
					continue;
					
				}
			}
			
			try{
				
				// here process debug entry
				for(DebugProcessor processor : processors) {
					processor.onEntry(entry);
				}
				
			} catch(Exception e) {
				
				e.printStackTrace();
				
			}
		}
		
	}
	
	

}
