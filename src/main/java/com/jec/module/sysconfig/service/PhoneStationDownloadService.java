package com.jec.module.sysconfig.service;

import com.googlecode.genericdao.search.Search;
import com.jec.module.sysconfig.command.PhoneStationCommand;
import com.jec.module.sysconfig.dao.PhoneStationDao;
import com.jec.module.sysconfig.entity.PhoneStation;
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
 * Created by jeremyliu on 08/10/2016.
 */
@Service
public class PhoneStationDownloadService extends DownloadService{

    @Resource
    private PhoneStationDao phoneStationDao;

    @Resource
    private SysLogService sysLogService;

    protected Response download(NetUnit netUnit, List<PhoneStation> configs){
        int netUnitId = netUnit.getId();

        if(!netStateService.isOnline(netUnitId))
            return Response.Builder().status(Response.STATUS_PARTIAL_SUCCESS).message(netUnit.getName() + "不在线");
        executor.setRemoteAddress(netUnit.getIp(), netUnit.getPort());
        Command command;
        Response response = Response.Builder();
        String desc = netUnit.getName()+"会议配置失败:";
        Card mainCard = cardDao.getMainCard(netUnit.getId());
        if(mainCard == null)
            return response.status(Response.STATUS_PARAM_ERROR);

        for(PhoneStation config: configs){
            command = new PhoneStationCommand(netUnit.getNetId(), config, mainCard.getSlotNumber());
            Result result = executor.execute(command);
            if(!result.isSucceed()){
                response.status(Response.STATUS_PARTIAL_SUCCESS);
                desc += config.getName() + ",";
            }
        }

        if((Integer)response.get("status") != Response.STATUS_SUCCESS){
            desc = desc.substring(0, desc.length() - 1);
            response.message(desc);
        }

        desc="下载所有网元的会议配置,";
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
        List<PhoneStation> configs = phoneStationDao.findAll();
        Map<Integer, List<PhoneStation>> map = new HashMap<>();
        Map<Integer, NetUnit> netUnitMap = new HashMap<>();

        for(PhoneStation config : configs){
            List<PhoneStation> list = map.get(config.getNetunit());
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
            for(Map.Entry<Integer, List<PhoneStation>> entry : map.entrySet()){
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
        Search search = new Search(PhoneStation.class);
        search.addFilterEqual("netunit", netUnitId);
        List<PhoneStation> configs =  phoneStationDao.search(search);
        Response resp;
        synchronized (executor) {
            resp = download(netUnit, configs);
        }
        String desc="下载"+netUnit.getName()+"的会议配置,";
        if(resp.isSuccess())
            desc+="下载成功";
        else
            desc+=resp.getMessage();
        sysLogService.addLog(desc);
        return resp;
    }
}
