package com.jec.protocol.command;


public abstract class CommandT<INPUT, OUTPUT> extends Command {
	
	/**
	 * 指令参数
	 */
	protected INPUT input = null;
	
	/**
	 * 指令结果
	 */
	protected OUTPUT output = null;
	
	
	
	

	public INPUT getInput() {
		return input;
	}

	public void setInput(INPUT input) {
		this.input = input;
	}

	public OUTPUT getOutput() {
		return output;
	}

	public CommandT() {
	}
}
