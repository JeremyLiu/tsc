package com.jec.module.sysconfig.service;

import com.jec.protocol.command.CommandExecutor;

/**
 * Created by jeremyliu on 7/2/16.
 */
public abstract class DownloadService extends Thread{

    protected CommandExecutor executor = new CommandExecutor();

    private String stateText;

    public String getStateText() {
        return stateText;
    }

    protected synchronized void setStateText(String stateText) {
        this.stateText = stateText;
    }

    abstract boolean enable();
}
