package com.jec.module.sysconfig.service;

import com.jec.module.sysconfig.command.TerminalKeyCommand;
import com.jec.module.sysconfig.dao.TerminalKeyConfigViewDao;
import com.jec.module.sysconfig.entity.vo.TerminalKeyConfigView;
import com.jec.module.sysmanage.service.SysLogService;
import com.jec.module.sysmonitor.entity.Card;
import com.jec.module.sysmonitor.entity.NetUnit;
import com.jec.protocol.command.Command;
import com.jec.protocol.command.Result;
import com.jec.utils.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jeremyliu on 09/10/2016.
 */
@Service
public class TerminalKeyDownloadService extends DeviceDownloadService {

    @Resource
    private TerminalKeyConfigViewDao terminalKeyConfigViewDao;

    @Resource
    private SysLogService sysLogService;


    protected Response download(NetUnit netUnit, List<TerminalKeyConfigView> configs){

        Map<String, List<TerminalKeyConfigView>> configMap = new HashMap<>();
        for(TerminalKeyConfigView config: configs){
            if(configMap.containsKey(config.getDeviceNumber())){
                configMap.get(config.getDeviceNumber()).add(config);
            }else{
                List<TerminalKeyConfigView> list = new ArrayList<>();
                list.add(config);
                configMap.put(config.getDeviceNumber(),list);
            }
        }

        int netUnitId = netUnit.getId();
        Response response = Response.Builder();

        if(!netStateService.isOnline(netUnitId))
            return response.status(Response.STATUS_PARTIAL_SUCCESS).message(netUnit.getName() + "不在线");
        executor.setRemoteAddress(netUnit.getIp(), netUnit.getPort());

        if(configs.size()==0)
            return response.status(Response.STATUS_FAIL).message(netUnit.getName()+"终端无按键配置可以下载");

        String desc = netUnit.getName()+"终端按键配置失败:";
        Card mainCard = cardDao.getMainCard(netUnit.getId());
        if(mainCard == null)
            return response.status(Response.STATUS_PARAM_ERROR);

        executor.setRemoteAddress(netUnit.getIp(), netUnit.getPort());

        for(Map.Entry<String, List<TerminalKeyConfigView>> entry: configMap.entrySet()) {
            List<TerminalKeyConfigView> list = entry.getValue();
            Command command = new TerminalKeyCommand(netUnit.getNetId(), list, mainCard.getSlotNumber());
            Result result = executor.execute(command);
            if (!result.isSucceed()) {
                response.status(Response.STATUS_PARTIAL_SUCCESS);
                desc += list.get(0).getDeviceName() + ",";
            }
        }

        desc="下载"+netUnit.getName()+"终端按键配置,";
        if(response.isSuccess())
            desc+="下载成功";
        else
            desc+=response.getMessage();
        sysLogService.addLog(desc);
        return response;

    }

    @Override
    @Transactional(readOnly = true)
    public Response download() {
        List<NetUnit> netUnits = netUnitDao.findAll();
        List<TerminalKeyConfigView> configs = terminalKeyConfigViewDao.findAll();
        Map<Integer, List<TerminalKeyConfigView>> map = new HashMap<>();
        Map<Integer, NetUnit> netUnitMap = new HashMap<>();

        for(TerminalKeyConfigView config : configs){
            List<TerminalKeyConfigView> list = map.get(config.getNetunit());
            if(list == null) {
                list = new ArrayList<>();
                map.put(config.getNetunit(), list);
            }
            list.add(config);
        }

        for(NetUnit netUnit: netUnits)
            netUnitMap.put(netUnit.getId(), netUnit);

        Response response = Response.Builder();
        String desc = "";

        synchronized (executor){
            for(Map.Entry<Integer, List<TerminalKeyConfigView>> entry : map.entrySet()){
                NetUnit netUnit = netUnitMap.get(entry.getKey());
                Response res = download(netUnit, entry.getValue());
                if((Integer) res.get("status") != Response.STATUS_SUCCESS){
                    response.status(Response.STATUS_PARTIAL_SUCCESS);
                    desc += res.get("message") + ";";
                }
            }
        }

        if((Integer)response.get("status") != Response.STATUS_SUCCESS){
            desc = desc.substring(0,desc.length() - 1);
            response.message(desc);
        }

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Response download(int netUnitId) {

        NetUnit netUnit = netUnitDao.find(netUnitId);
        if(netUnit == null)
            return Response.Builder().status(Response.STATUS_PARAM_ERROR);

        List<TerminalKeyConfigView> configs =  terminalKeyConfigViewDao.findByNetUnit(netUnitId);
        Response resp;
        synchronized (executor) {
            resp = download(netUnit, configs);
        }
        String desc="下载"+netUnit.getName()+"的终端配置,";
        if(resp.isSuccess())
            desc+="下载成功";
        else
            desc+=resp.getMessage();
        sysLogService.addLog(desc);
        return resp;
    }

    @Override
    @Transactional
    public Response download(String number){
        Response resp = Response.Builder().status(Response.STATUS_PARAM_ERROR);

        List<TerminalKeyConfigView> configs =  terminalKeyConfigViewDao.findByDevice(number);

        if(configs.size()==0)
            return resp.message("终端无配置可以下载");

        int netUnitId = configs.get(0).getNetunit();
        NetUnit netUnit = netUnitDao.find(netUnitId);

        synchronized (executor) {
            resp = download(netUnit, configs);
        }
        String desc="下载"+configs.get(0).getDeviceName()+"的终端按键配置,";
        if(resp.isSuccess())
            desc+="下载成功";
        else
            desc+=resp.getMessage();
        sysLogService.addLog(desc);
        return resp;
    }
}
