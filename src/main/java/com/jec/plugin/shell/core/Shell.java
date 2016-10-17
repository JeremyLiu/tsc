package com.jec.plugin.shell.core;

import java.util.LinkedList;
import java.util.List;

import com.jec.plugin.framework.Framework;
import com.jec.plugin.framework.Plugin;
import com.jec.plugin.shell.ShellModule;
import com.jec.plugin.shell.ShellService;
import com.jec.plugin.shell.core.telnet.Listener;

public class Shell implements Plugin, ShellService {
	
	static List<Module> modules = new LinkedList<Module>();
	
	public static List<Module> getModules() {
		return modules;
	}
	
	
	private Listener listener;

	@Override
	public String getAuthor() {
		return "lingdm";
	}

	@Override
	public String getDescription() {
		return "Support telnet shell command.";
	}

	@Override
	public String getName() {
		return "TelnetShell";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public void start() throws Exception {
		
		listener = new Listener();
		listener.activate();
		
		//
		Framework.registerService(ShellService.class, this);
		
	}

	@Override
	public void stop() {	
		
		if(listener != null) {
			listener.deactivate();
			listener = null;
		}
		
	}

	@Override
	public ShellModule createModule(String name, String summary) {
		Module module = new Module(name, summary);
		if(!modules.contains(module)) {
			modules.add(module);
			return module;
		}
		
		System.out.println("Shell : Module named '" + name + "' has been used.");
		return null;
	}

	@Override
	public void removeModule(ShellModule module) {
		modules.remove(module);
	}

	
	
	
	
}
