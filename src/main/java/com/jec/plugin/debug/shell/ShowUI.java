package com.jec.plugin.debug.shell;

import java.io.PrintStream;

import com.jec.plugin.debug.view.DebugViewer;
import com.jec.plugin.shell.ShellCmdLine;
import com.jec.plugin.shell.ShellCommand;

public class ShowUI implements ShellCommand {
	
	private DebugViewer viewer = null;

	public ShowUI(DebugViewer viewer) {
		super();
		this.viewer = viewer;
	}

	@Override
	public String gethelp() {
		return "Show the debug viewer";
	}

	@Override
	public String getName() {
		return "show";
	}

	@Override
	public String getUsage() {
		return "Usage: show ";
	}

	@Override
	public void process(ShellCmdLine line, PrintStream out) {
		viewer.setVisible(true);
	}

}
