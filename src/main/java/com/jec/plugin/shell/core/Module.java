package com.jec.plugin.shell.core;

import java.util.LinkedList;
import java.util.List;

import com.jec.plugin.shell.ShellCommand;
import com.jec.plugin.shell.ShellModule;

public class Module implements ShellModule{
	
	private String name = null;
	
	private String summary = null;

	private List<ShellCommand> commands = 
		new LinkedList<ShellCommand>();

	public Module(String name, String summary) {
		super();
		this.name = name;
		this.summary = summary;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getSummary() {
		return summary;
	}

	@Override
	public List<ShellCommand> getCommands() {
		return commands;
	}

	@Override
	public void addCommand(ShellCommand command) {
		if(command != null && !commands.contains(command)) {
			commands.add(command);
		}
	}

	@Override
	public void removeCommand(ShellCommand command) {
		if(command != null) {
			commands.remove(command);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Module other = (Module) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	
	
	
}
