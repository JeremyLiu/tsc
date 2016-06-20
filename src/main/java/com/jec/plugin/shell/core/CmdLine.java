package com.jec.plugin.shell.core;

import java.util.HashMap;
import java.util.Map;

import com.jec.plugin.shell.ShellCmdLine;

public class CmdLine implements ShellCmdLine {
	
	private String name;
	
	private Map<String, String> parameters = null;

	private CmdLine(String name, Map<String, String> parameters) {
		super();
		this.name = name;
		this.parameters = parameters;
	}
	
	public boolean equalString(String text) {
		return name.equalsIgnoreCase(text);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getParameter(String key) {
		return parameters.get(key);
	}
	
	public static CmdLine parse(String line) throws Exception {
		
		/*
		 *	从字符串中解析出 CommanLine对象，字符串格式：
		 *	[name] [key1]=[value1] [key2]=[value2] ...... [keyN]=[valueN]
		 */
		
		if(line == null || line.isEmpty()) {
			return null;
		}
		
		
		String[] args = line.split(" ");
		
		if(args == null || args.length == 0) {
			throw new RuntimeException("Can't split any args.");
		}
		
		// get name and check empty
		String name = args[0].trim();
		if(name.isEmpty()) {
			throw new RuntimeException("Empty command name.");
		}
		
		// get parameters
		Map<String, String> parameters = new HashMap<String, String>();
		for(int i = 1; i < args.length; i++) {
			
			String temp = args[i];
			temp = temp.trim();
			if(temp.isEmpty()) {
				continue;
			}
			
			String[] ps = temp.split("=", 2);
			if(ps.length != 2) {
				throw new RuntimeException("Wrong parameter format. -> " + temp);
			}
			
			if(ps[0].isEmpty()) {
				throw new RuntimeException("Wrong parameter key. -> " + temp);
			}
			
			if(ps[1].isEmpty()) {
				throw new RuntimeException("Wrong parameter value. -> " + temp);
			}
			
			parameters.put(ps[0], ps[1]);
			
		}
		
		
		return new CmdLine(name, parameters);
	}

}
