package com.jec.plugin.shell;

import java.io.PrintStream;

public interface ShellCommand {
	
	String getName();
	
	String gethelp();
	
	String getUsage();
	
	/**
	 * 处理命令的函数，该函数内需要对命令结构、参数等进行检验
	 * @param line		shell终端发过来的命令
	 * @param printer 	用来将回应信息打印到shell终端
	 */
	void process(ShellCmdLine line, PrintStream out);
	
}
