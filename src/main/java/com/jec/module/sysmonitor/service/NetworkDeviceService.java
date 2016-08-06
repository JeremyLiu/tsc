package com.jec.module.sysmonitor.service;

import com.googlecode.genericdao.search.Search;
import com.jec.module.sysmonitor.dao.DevicePortDao;
import com.jec.module.sysmonitor.dao.NetUnitDao;
import com.jec.module.sysmonitor.dao.TerminalDeviceDao;
import com.jec.module.sysmonitor.dao.TerminalDeviceViewDao;
import com.jec.module.sysmonitor.entity.DevicePort;
import com.jec.module.sysmonitor.entity.TerminalDevice;
import com.jec.module.sysmonitor.entity.view.TerminalDeviceView;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by jeremyliu on 6/3/16.
 */
@Service
public class NetworkDeviceService {

    @Resource
    private TerminalDeviceViewDao terminalDeviceViewDao;

    @Resource
    private TerminalDeviceDao terminalDeviceDao;

    @Resource
    private NetUnitDao netUnitDao;

    @Resource
    private DevicePortDao devicePortDao;

    @Transactional(readOnly = true)
    public List<TerminalDeviceView> getAllDevice(){
        return terminalDeviceViewDao.findAll();
    }

    @Transactional
    public int createDevice(int netUnitId,String name, int slot, int port){
        if(netUnitDao.find(netUnitId) == null)
            return -1;

        TerminalDevice terminalDevice = new TerminalDevice();
        terminalDevice.setName(name);
        terminalDevice.setCardId(slot);
        terminalDevice.setCardPort(port);
        terminalDevice.setNetUnitId(netUnitId);

        terminalDeviceDao.save(terminalDevice);
        return terminalDevice.getId();
    }

    @Transactional
    public boolean modifyDevice(int id, Integer netUnitId, String name, Integer slot, Integer port){
        TerminalDevice terminalDevice = terminalDeviceDao.find(id);
        if(terminalDevice == null)
            return false;

        if(netUnitDao.find(netUnitId) == null)
            return false;
        if(name != null)
            terminalDevice.setName(name);
        if(slot != null)
            terminalDevice.setCardId(slot);
        if(port != null)
            terminalDevice.setCardPort(port);
        if(netUnitId != null)
            terminalDevice.setNetUnitId(netUnitId);
        return true;
    }

    @Transactional
    public boolean removeDevice(int id){
        Search search = new Search(DevicePort.class);
        search.addFilterEqual("deviceId", id);
        try {
            List<DevicePort> ports = devicePortDao.search(search);
            devicePortDao.remove(ports.toArray(new DevicePort[ports.size()]));
            terminalDeviceDao.removeById(id);
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Transactional(readOnly = true)
    public List<DevicePort> getDevicePort(int deviceId){
        Search search = new Search(DevicePort.class);
        search.addFilterEqual("deviceId", deviceId);
        return devicePortDao.search(search);
    }

    @Transactional
    public int createDevicePort(int deviceId, int number, String function){
        if(terminalDeviceDao.find(deviceId) == null)
            return -1;
        if(devicePortDao.find(number) != null)
            return -2;
        DevicePort devicePort = new DevicePort();
        devicePort.setId(number);
        devicePort.setDeviceId(deviceId);
        devicePort.setFunction(function);
        devicePortDao.save(devicePort);

        return devicePort.getId();
    }

    @Transactional
    public boolean modifyDevicePort(int oldId, Integer number, String function){
        if(number != null && devicePortDao.find(number) != null)
            return false;
        return devicePortDao.updatePort(oldId,number,function) > 0;
    }

    @Transactional
    public boolean removeDevicePort(int id){
        return devicePortDao.removeById(id);
    }
}
