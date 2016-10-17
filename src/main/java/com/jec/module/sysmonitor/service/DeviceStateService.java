package com.jec.module.sysmonitor.service;

import com.jec.base.entity.NetState;
import com.jec.module.sysmonitor.dao.NetUnitDao;
import com.jec.module.sysmonitor.dao.TerminalDeviceDao;
import com.jec.module.sysmonitor.dao.TerminalDeviceViewDao;
import com.jec.module.sysmonitor.entity.NetUnit;
import com.jec.module.sysmonitor.entity.TerminalDevice;
import com.jec.module.sysmonitor.entity.view.TerminalDeviceView;
import com.jec.protocol.command.CommandExecutor;
import com.jec.protocol.command.DeviceCommand;
import com.jec.protocol.command.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jeremyliu on 5/19/16.
 */
@Service
public class DeviceStateService {

    @Resource
    private TerminalDeviceViewDao terminalDeviceViewDao;

    @Resource
    private NetUnitDao netUnitDao;

    private Map<String, Integer> deviceState = new HashMap<>();

    @Transactional(readOnly = true)
    public List<NetState> queryDevice(){
//        CommandExecutor ce = new CommandExecutor(false);
//        Result result = ce.open();
//        if(!result.isSucceed()) {
//            System.err.println(result.getDescription());
//            return new ArrayList<>();
//        }

        List<TerminalDeviceView> devices = terminalDeviceViewDao.findAll();
        List<NetState> netStates = new ArrayList<>(devices.size());
        for(TerminalDeviceView device: devices){
//            int netUnitId = device.getNetUnitId();
//            NetUnit netUnit = netUnitDao.find(netUnitId);
//            String ip = netUnit.getIp();
//            ce.setRemoteAddress(ip, netUnit.getPort());
//            ce.setTimeout(2000);
//            DeviceCommand deviceCommand = new DeviceCommand(netUnitId,7,device.getDeafultPort());
//            deviceCommand.setNeedResponse(false);
//
//            result = ce.execute(deviceCommand);

            NetState netState = new NetState();
            netState.setId(device.getId());
            int state = NetState.UNKNOWN;
            if(deviceState.containsKey(device.getCode()))
                state = deviceState.get(device.getCode());
            netState.setState(state);
            netStates.add(netState);

        }
        return netStates;
    }

    public void setDeviceState(String number, int state){
        deviceState.put(number,NetState.deviceStateMap(state));
    }

    public void refreshState(NetUnit netunit){

        CommandExecutor ce = new CommandExecutor(false);
        Result result = ce.open();
        if(!result.isSucceed()) {
            System.err.println(result.getDescription());
            return;
        }
        String ip = netunit.getIp();
        ce.setRemoteAddress(ip, netunit.getPort());
        ce.setTimeout(2000);

        List<TerminalDeviceView> devices = terminalDeviceViewDao.findByNetunit(netunit.getId());
        for(TerminalDeviceView device: devices){

            DeviceCommand deviceCommand = new DeviceCommand(netunit.getNetId(),7,device.getDeafultPort());
            deviceCommand.setNeedResponse(false);
            ce.execute(deviceCommand);
        }
    }

    @Transactional(readOnly = true)
    public void refresh(){
        List<NetUnit> netUnits = netUnitDao.findAll();
        for(NetUnit netUnit : netUnits) {
            refreshState(netUnit);
        }
    }
    @Transactional(readOnly = true)
    public void refresh(int netunit){
        NetUnit netUnit = netUnitDao.find(netunit);
        if(netUnit == null)
            return;
        refreshState(netUnit);
    }


    @Transactional(readOnly = true)
    public List<NetState> queryDevice(int netunitId){
        CommandExecutor ce = new CommandExecutor(false);
        Result result = ce.open();
        if(!result.isSucceed()) {
            System.err.println(result.getDescription());
            return new ArrayList<>();
        }
        NetUnit netUnit = netUnitDao.find(netunitId);
        if(netUnit == null)
            return new ArrayList<>();
        String ip = netUnit.getIp();
        ce.setRemoteAddress(ip, netUnit.getPort());
        ce.setTimeout(2000);

        List<TerminalDeviceView> devices = terminalDeviceViewDao.findByNetunit(netunitId);
        List<NetState> netStates = new ArrayList<>(devices.size());
        for(TerminalDeviceView device: devices){
            int netUnitId = device.getNetUnitId();
            DeviceCommand deviceCommand = new DeviceCommand(netUnitId,7,device.getDeafultPort());
            deviceCommand.setNeedResponse(false);
            result = ce.execute(deviceCommand);

            NetState netState = new NetState();
            netState.setId(device.getId());

            if(result.isSucceed()){
                netState.setState(deviceCommand.getState());
            }else
                netState.setState(NetState.DS_FAILURE);

            netStates.add(netState);
        }
        return netStates;
    }
}
