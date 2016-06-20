package com.jec.plugin.shell;

import java.util.List;

public interface ShellModule {
	
	String getName();
	
	String getSummary();
	
	List<ShellCommand> getCommands();
	
	void addCommand(ShellCommand command);
	
	void removeCommand(ShellCommand command);
	
}
