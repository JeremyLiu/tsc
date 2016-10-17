package com.jec.module.sysmonitor.service;

import com.googlecode.genericdao.search.Search;
import com.jec.module.sysconfig.dao.TerminalBusinessViewDao;
import com.jec.module.sysconfig.dao.TerminalKeyConfigDao;
import com.jec.module.sysconfig.dao.TerminalKeyConfigViewDao;
import com.jec.module.sysconfig.entity.TerminalKeyConfig;
import com.jec.module.sysconfig.entity.vo.TerminalBusinessView;
import com.jec.module.sysconfig.entity.vo.TerminalKeyConfigView;
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

    @Resource
    private TerminalBusinessViewDao terminalBusinessViewDao;

    @Resource
    private TerminalKeyConfigViewDao terminalKeyConfigViewDao;

    @Transactional(readOnly = true)
    public List<TerminalDeviceView> getAllDevice(int netunit){
        if(netunit>0)
            return terminalDeviceViewDao.findByNetunit(netunit);
        else
            return terminalDeviceViewDao.findAll();
    }

    @Transactional
    public int createDevice(int netUnitId,String name, String code){
        if(netUnitDao.find(netUnitId) == null)
            return -1;

        TerminalDevice terminalDevice = new TerminalDevice();
        terminalDevice.setName(name);
        terminalDevice.setNetUnitId(netUnitId);
        terminalDevice.setCode(code);
        terminalDeviceDao.save(terminalDevice);
        return terminalDevice.getId();
    }

    @Transactional
    public boolean modifyDevice(int id, Integer netUnitId, String name, String code){
        TerminalDevice terminalDevice = terminalDeviceDao.find(id);
        if(terminalDevice == null)
            return false;

        if(netUnitDao.find(netUnitId) == null)
            return false;
        if(name != null)
            terminalDevice.setName(name);
        if(netUnitId != null)
            terminalDevice.setNetUnitId(netUnitId);
        if(code!=null)
            terminalDevice.setCode(code);
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
    public int createDevicePort(int deviceId, String number, String function, boolean enable){
        if(terminalDeviceDao.find(deviceId) == null)
            return -1;
        DevicePort devicePort = new DevicePort();
        devicePort.setNumber(number);
        devicePort.setDeviceId(deviceId);
        if(devicePortDao.exist(devicePort))
            return -2;
        devicePort.setFunction(function);
        devicePort.setEnable(enable);
        devicePortDao.save(devicePort);

        return devicePort.getId();
    }

    @Transactional
    public boolean modifyDevicePort(int oldId, String number, String function,boolean enable){
        DevicePort devicePort = devicePortDao.find(oldId);
        if(devicePort == null)
            return false;
        if(number != null && devicePortDao.exist(devicePort))
            return false;
        return devicePortDao.updatePort(oldId,number,function, enable) > 0;
    }

    @Transactional
    public boolean removeDevicePort(int id){
        return devicePortDao.removeById(id);
    }

    @Transactional(readOnly = true)
    public List<TerminalBusinessView> getTerminalBusiness(String number){
        return terminalBusinessViewDao.findByDevice(number);
    }

    @Transactional(readOnly = true)
    public List<TerminalKeyConfigView> getTerminalKeyConfig(String number){
        return terminalKeyConfigViewDao.findByDevice(number);
    }
}
