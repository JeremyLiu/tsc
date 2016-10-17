package com.jec.plugin.shell;

public interface ShellService {

	/**
	 * Create a new ShellModule to shell root level
	 * @param name
	 * @param summary
	 * @return the module created, if create failed, null returned
	 */
	ShellModule createModule(String name, String summary);
	
	/**
	 * Remove a ShellModule from shell root level
	 * @param module
	 */
	void removeModule(ShellModule module);
	
	
}
