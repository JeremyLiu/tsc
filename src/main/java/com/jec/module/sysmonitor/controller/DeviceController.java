package com.jec.module.sysmonitor.controller;

import com.jec.module.sysmonitor.service.DeviceStateService;
import com.jec.module.sysmonitor.service.NetworkDeviceService;
import com.jec.protocol.unit.BCD;
import com.jec.utils.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Created by jeremyliu on 6/5/16.
 */
@Controller
@RequestMapping(value = "/device")
public class DeviceController {

    @Resource
    private NetworkDeviceService networkDeviceService;

    @Resource
    private DeviceStateService deviceStateService;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    Response index(@RequestParam(value="netunit", required = false, defaultValue = "-1") int netunit){
        return Response.Builder(networkDeviceService.getAllDevice(netunit));
    }

    @RequestMapping(method = RequestMethod.GET, value = "state")
    public @ResponseBody
    Response state(@RequestParam(value="netunit", required = false, defaultValue = "-1") int netunit){
        if(netunit<=0)
            return Response.Builder(deviceStateService.queryDevice());
        else
            return Response.Builder(deviceStateService.queryDevice(netunit));
    }

    @RequestMapping(method = RequestMethod.POST, value = "create")
    public @ResponseBody
    Response createDevice(@RequestParam(value="name") String name,
                          @RequestParam(value="code") String code,
                           @RequestParam(value="netunit") int netUnit){
        Response res = Response.Builder().status(Response.STATUS_PARAM_ERROR);
        if(name.length() == 0 || name.length() >60)
            return res.message("设备名字不合法");

        int id = networkDeviceService.createDevice(netUnit,name,code);

        if(id == -1)
            return res.message("网元不存在");

        return res.status(Response.STATUS_SUCCESS).data(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "modify")
    public @ResponseBody
    Response modifyDevice(@RequestParam(value="id") int id,
                          @RequestParam(value="name", required = false) String name,
                          @RequestParam(value="code", required = false) String code,
                          @RequestParam(value="netunit", required = false) Integer netunit){
        return Response.Builder(networkDeviceService.modifyDevice(id,netunit, name, code));
    }

    @RequestMapping(method =  RequestMethod.DELETE, value = "remove")
    public @ResponseBody
    Response removeDevice(@RequestParam(value="id") int id){
        return Response.Builder().data(networkDeviceService.removeDevice(id));
    }

    @RequestMapping(method = RequestMethod.GET, value = "port")
    public @ResponseBody
    Response getDevicePort(@RequestParam(value="id") int deviceId){
        return Response.Builder(networkDeviceService.getDevicePort(deviceId));
    }

    @RequestMapping(method = RequestMethod.POST, value = "port/create")
    public @ResponseBody
    Response createDevicePort(@RequestParam(value="deviceId") int deviceId,
                              @RequestParam(value="number") String number,
                              @RequestParam(value="enable") boolean enable,
                              @RequestParam(value="function") String function){
        Response res = Response.Builder().status(Response.STATUS_PARAM_ERROR);
        if(function.length() == 0 || function.length()>100)
            return res.message("功能格式错误");
        if(BCD.fromString(number)==null)
            return res.message("终端号码格式错误");
        int result = networkDeviceService.createDevicePort(deviceId,number, function, enable);
        if(result == -1)
            return res.message("设备不存在");
        if(result == -2)
            return res.message("终端号码重复");
        return res.status(Response.STATUS_SUCCESS);
    }

    @RequestMapping(method = RequestMethod.POST, value = "port/modify")
    public @ResponseBody
    Response modifyDevicePort(@RequestParam(value="id") int id,
                              @RequestParam(value="enable", required = false, defaultValue = "false") boolean enable,
                              @RequestParam(value="number", required = false) String number,
                              @RequestParam(value="function", required = false) String function){
        return Response.Builder().data(networkDeviceService.modifyDevicePort(id, number,function, enable));
    }

    @RequestMapping(method =  RequestMethod.DELETE, value = "port/remove")
    public @ResponseBody
    Response removeDevicePort(@RequestParam(value="id") int id){
        return Response.Builder().data(networkDeviceService.removeDevicePort(id));
    }

    @RequestMapping(value = "config/{type}")
    public @ResponseBody
    Response getConfig(@RequestParam(value="number") String number,
                       @PathVariable(value="type") String type){
        if(type.equals("terminalbusiness"))
            return Response.Builder().data(networkDeviceService.getTerminalBusiness(number));
        else if(type.equals("terminalkey"))
            return Response.Builder().data(networkDeviceService.getTerminalKeyConfig(number));
        else
            return Response.Builder().status(Response.STATUS_PARAM_ERROR);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/refresh")
    public @ResponseBody
    Response refresh(@RequestParam(value="netunit", required = false) Integer netunit){
        if(netunit == null || netunit<=0)
            deviceStateService.refresh();
        else
            deviceStateService.refresh(netunit);
        return Response.Builder();
    }
}
