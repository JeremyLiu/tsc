package com.jec.module.sysconfig.service;

import com.jec.module.sysconfig.command.ClockCommand;
import com.jec.module.sysconfig.dao.ClockDao;
import com.jec.module.sysconfig.entity.Clock;
import com.jec.module.sysmanage.service.SysLogService;
import com.jec.module.sysmonitor.entity.Card;
import com.jec.module.sysmonitor.entity.NetUnit;
import com.jec.protocol.command.Command;
import com.jec.protocol.command.CommandExecutor;
import com.jec.protocol.command.Result;
import com.jec.utils.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by jeremyliu on 8/6/16.
 */
@Service
public class ClockDownloadService extends DownloadService {

    @Resource
    private ClockDao clockDao;

    @Resource
    private SysLogService sysLogService;

    @Transactional(readOnly = true)
    public Response downloadClock(NetUnit netUnit, Clock clock){

        int netUnitId = netUnit.getId();

        if(!netStateService.isOnline(netUnitId))
            return Response.Builder().status(Response.STATUS_PARTIAL_SUCCESS).message(netUnit.getName() + "不在线");

        executor.setRemoteAddress(netUnit.getIp(), netUnit.getPort());
        Card mainCard = cardDao.getMainCard(netUnit.getNetId());
        if(mainCard == null){
            return Response.Builder().status(Response.STATUS_PARAM_ERROR).message(netUnit.getName()+"未配置主板卡");
        }
        Command cmd = new ClockCommand(netUnit.getNetId(), clock, mainCard.getSlotNumber());
        cmd.setNeedResponse(true);
        Result result = executor.execute(cmd);
        if(result.isSucceed())
            return Response.Builder(true);
        else
            return Response.Builder().status(Response.STATUS_FAIL).message(netUnit.getName());
    }

    @Transactional(readOnly = true)
    @Override
    public Response download(int netUnitId){
        NetUnit netUnit = netUnitDao.find(netUnitId);
        Clock clock = clockDao.find(netUnitId);
        if(netUnit == null || clock == null)
            return Response.Builder().status(Response.STATUS_PARAM_ERROR);
        Response resp;
        synchronized (executor) {
            resp = downloadClock(netUnit, clock);
        }
        String desc="下载"+netUnit.getName()+"的时钟配置,";
        if(resp.isSuccess())
            desc+="下载成功";
        else
            desc+=resp.getMessage();
        sysLogService.addLog(desc);
        return resp;
    }

    @Override
    @Transactional(readOnly = true)
    public Response download(){
        List<Clock> clocks = clockDao.findAll();
        String desc = "";
        Response result = Response.Builder();
        synchronized (executor) {
            for (Clock clock : clocks) {
                NetUnit netUnit = netUnitDao.find(clock.getNetunit());
                Response response = downloadClock(netUnit, clock);
                if ((Integer) response.get("status") != Response.STATUS_SUCCESS) {
                    result.status(Response.STATUS_PARTIAL_SUCCESS);
                    desc += response.get("message") + ",";
                }
            }
        }
        if((Integer)result.get("status") != Response.STATUS_SUCCESS){
            desc = desc.substring(0, desc.length() - 1) + "时钟设置失败";
            result.message(desc);
        }
        desc="下载所有网元的时钟配置,";
        if(result.isSuccess())
            desc+="下载成功";
        else
            desc+=result.getMessage();
        sysLogService.addLog(desc);
        return result;
    }
}
