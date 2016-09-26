package com.jec.module.sysconfig.service;

import com.googlecode.genericdao.search.Search;
import com.jec.module.sysconfig.command.DigitTrunkCommand;
import com.jec.module.sysconfig.dao.DigitTrunkDao;
import com.jec.module.sysconfig.entity.DigitTrunk;
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
 * Created by jeremyliu on 9/25/16.
 */
@Service
public class DigitTrunkDownloadService extends DownloadService{

    @Resource
    private DigitTrunkDao digitTrunkDao;

    protected Response download(NetUnit netUnit, List<DigitTrunk> configs){

        int netUnitId = netUnit.getId();

        if(!netStateService.isOnline(netUnitId))
            return Response.Builder(Response.STATUS_PARTIAL_SUCCESS).message(netUnit.getName() + "不在线");
        executor.setRemoteAddress(netUnit.getIp(), netUnit.getPort());
        Command command;
        Response response = Response.Builder();
        String desc = netUnit.getName()+"数字中继配置失败:";
        Card mainCard = cardDao.getMainCard(netUnit.getId());
        if(mainCard == null)
            return response.status(Response.STATUS_PARAM_ERROR);

        for(DigitTrunk config: configs){
            command = new DigitTrunkCommand(config, mainCard.getSlotNumber(), netUnit.getNetId());
            Result result = executor.execute(command);
            if(!result.isSucceed()){
                response.status(Response.STATUS_PARTIAL_SUCCESS);
                desc += "网元:"+ netUnit.getName()+",板卡槽位:" + config.getSlot() +
                        ",端口:"+ config.getPort() + "\n";
            }
        }

        if((Integer)response.get("status") != Response.STATUS_SUCCESS){
            desc = desc.substring(0, desc.length() - 1);
            response.message(desc);
        }

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public Response download() {

        List<NetUnit> netUnits = netUnitDao.findAll();
        List<DigitTrunk> digitTrunkConfigs = digitTrunkDao.findAll();
        Map<Integer, List<DigitTrunk>> map = new HashMap<>();
        Map<Integer, NetUnit> netUnitMap = new HashMap<>();

        for(DigitTrunk config : digitTrunkConfigs){
            List<DigitTrunk> list = map.get(config.getNetunit());
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
            for(Map.Entry<Integer, List<DigitTrunk>> entry : map.entrySet()){
                NetUnit netUnit = netUnitMap.get(entry.getKey());
                Response res = download(netUnit, entry.getValue());
                if((Integer) res.get("status") != Response.STATUS_SUCCESS){
                    response.status(Response.STATUS_PARTIAL_SUCCESS);
                    desc += res.get("message") + "\n";
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
    public Response download(int netUnitId) {
        NetUnit netUnit = netUnitDao.find(netUnitId);
        if(netUnit == null)
            return Response.Builder().status(Response.STATUS_PARAM_ERROR);
        Search search = new Search(DigitTrunk.class);
        search.addFilterEqual("netunit", netUnitId);
        List<DigitTrunk> digitTrunks =  digitTrunkDao.search(search);
        synchronized (executor) {
            return download(netUnit, digitTrunks);
        }
    }
}
