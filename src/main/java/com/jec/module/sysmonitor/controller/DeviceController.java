package com.jec.module.sysmonitor.controller;

import com.jec.module.sysmonitor.service.DeviceStateService;
import com.jec.module.sysmonitor.service.NetworkDeviceService;
import com.jec.utils.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    Response index(){
        return Response.Builder(networkDeviceService.getAllDevice());
    }

    @RequestMapping(method = RequestMethod.GET, value = "state")
    public @ResponseBody
    Response state(){
        return Response.Builder(deviceStateService.queryDevice());
    }

    @RequestMapping(method = RequestMethod.POST, value = "create")
    public @ResponseBody
    Response createDevice(@RequestParam(value="name") String name,
                           @RequestParam(value="netunit") int netUnit){
        Response res = Response.Builder().status(Response.STATUS_PARAM_ERROR);
        if(name.length() == 0 || name.length() >60)
            return res.message("设备名字不合法");

        int id = networkDeviceService.createDevice(netUnit,name,0,0);

        if(id == -1)
            return res.message("网元不存在");

        return res.status(Response.STATUS_SUCCESS).data(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "modify")
    public @ResponseBody
    Response modifyDevice(@RequestParam(value="id") int id,
                          @RequestParam(value="name", required = false) String name,
                          @RequestParam(value="netunit", required = false) Integer netunit){
        return Response.Builder(networkDeviceService.modifyDevice(id,netunit, name, null, null));
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
                              @RequestParam(value="id") int id,
                              @RequestParam(value="function") String function){
        Response res = Response.Builder().status(Response.STATUS_PARAM_ERROR);
        if(function.length() == 0 || function.length()>100)
            return res.message("功能不合法");

        int result = networkDeviceService.createDevicePort(deviceId,id,function);
        if(result == -1)
            return res.message("设备不存在");
        if(result == -2)
            return res.message("设备编号重复");
        return res.status(Response.STATUS_SUCCESS);
    }

    @RequestMapping(method = RequestMethod.POST, value = "port/modify")
    public @ResponseBody
    Response modifyDevicePort(@RequestParam(value="id") int id,
                              @RequestParam(value="number", required = false) Integer number,
                              @RequestParam(value="function", required = false) String function){
        return Response.Builder().data(networkDeviceService.modifyDevicePort(id, number,function));
    }

    @RequestMapping(method =  RequestMethod.DELETE, value = "port/remove")
    public @ResponseBody
    Response removeDevicePort(@RequestParam(value="id") int id){
        return Response.Builder().data(networkDeviceService.removeDevicePort(id));
    }

}
