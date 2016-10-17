package com.jec.module.sysconfig.service;

import com.jec.module.sysmonitor.dao.CardDao;
import com.jec.module.sysmonitor.dao.NetUnitDao;
import com.jec.module.sysmonitor.service.NetWorkStateService;
import com.jec.protocol.command.CommandExecutor;
import com.jec.utils.Response;

import javax.annotation.Resource;

/**
 * Created by jeremyliu on 7/2/16.
 */
public abstract class DownloadService{

    @Resource
    protected NetUnitDao netUnitDao;

    @Resource
    protected CardDao cardDao;

    @Resource
    protected NetWorkStateService netStateService;

    protected CommandExecutor executor = new CommandExecutor();

    public abstract Response download();

    public abstract Response download(int netUnitId);
}
