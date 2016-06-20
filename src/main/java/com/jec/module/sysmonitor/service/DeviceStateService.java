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
import java.util.List;

/**
 * Created by jeremyliu on 5/19/16.
 */
@Service
public class DeviceStateService {

    @Resource
    private TerminalDeviceViewDao terminalDeviceViewDao;

    @Resource
    private NetUnitDao netUnitDao;

    @Transactional
    public List<NetState> queryDevice(){
        CommandExecutor ce = new CommandExecutor(false);
        Result result = ce.open();
        if(!result.isSucceed()) {
            System.err.println(result.getDescription());
            return new ArrayList<>();
        }

        List<TerminalDeviceView> devices = terminalDeviceViewDao.findAll();
        List<NetState> netStates = new ArrayList<>(devices.size());
        for(TerminalDeviceView device: devices){
            int netUnitId = device.getNetUnitId();
            NetUnit netUnit = netUnitDao.find(netUnitId);
            String ip = netUnit.getIp();
            ce.setRemoteAddress(ip, netUnit.getPort());
            ce.setTimeout(2000);
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
