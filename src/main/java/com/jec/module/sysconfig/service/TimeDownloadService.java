package com.jec.module.sysconfig.service;

import com.jec.module.sysconfig.command.TimeCommand;
import com.jec.module.sysmanage.service.SysLogService;
import com.jec.module.sysmonitor.entity.Card;
import com.jec.module.sysmonitor.entity.NetUnit;
import com.jec.protocol.command.Command;
import com.jec.protocol.command.Result;
import com.jec.utils.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by jeremyliu on 26/09/2016.
 */
@Service
public class TimeDownloadService extends DownloadService{

    @Resource
    private SysLogService sysLogService;

    protected Response download(NetUnit netUnit, long tempstamp){
        int netUnitId = netUnit.getId();

        if(!netStateService.isOnline(netUnitId))
            return Response.Builder().status(Response.STATUS_PARTIAL_SUCCESS).message(netUnit.getName() + "不在线");
        executor.setRemoteAddress(netUnit.getIp(), netUnit.getPort());
        Response response = Response.Builder();
        Card mainCard = cardDao.getMainCard(netUnit.getId());
        if(mainCard == null)
            return response.status(Response.STATUS_PARAM_ERROR);

        Command command = new TimeCommand(netUnit.getNetId(), mainCard.getSlotNumber(), tempstamp);
        command.setNeedResponse(true);
        Result result = executor.execute(command);
        if(result.isSucceed())
            return response.data(true);
        else
            return response.status(Response.STATUS_FAIL).message(netUnit.getName());
    }

    @Transactional(readOnly = true)
    public Response download(int netUnitId, long ts) {
        NetUnit netUnit = netUnitDao.find(netUnitId);
        if(netUnit == null)
            return Response.Builder().status(Response.STATUS_PARAM_ERROR);
        Response resp;
        synchronized (executor) {
            resp = download(netUnit, ts);
        }
        String desc="下载"+netUnit.getName()+"的时间配置,";
        if(resp.isSuccess())
            desc+="下载成功";
        else
            desc+=resp.getMessage();
        sysLogService.addLog(desc);
        return resp;
    }

    @Transactional(readOnly = true)
    public Response download(long ts){
        List<NetUnit> netUnits = netUnitDao.findAll();
        Response result = Response.Builder();
        String desc = "";
        synchronized (executor) {
            for(NetUnit netUnit : netUnits){
                Response response = download(netUnit, ts);
                if ((Integer) response.get("status") != Response.STATUS_SUCCESS) {
                    result.status(Response.STATUS_PARTIAL_SUCCESS);
                    desc += response.get("message") + ",";
                }
            }
        }

        if((Integer)result.get("status") != Response.STATUS_SUCCESS){
            desc = desc.substring(0, desc.length() - 1) + "时间配置失败";
            result.message(desc);
        }
        desc="下载所有网元的时间配置,";
        if(result.isSuccess())
            desc+="下载成功";
        else
            desc+=result.getMessage();
        sysLogService.addLog(desc);
        return result;
    }

    @Override
    public Response download() {
        return null;
    }

    @Override
    public Response download(int netUnitId) {
        return null;
    }
}
