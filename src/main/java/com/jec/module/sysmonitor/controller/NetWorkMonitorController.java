package com.jec.module.sysmonitor.controller;

import com.jec.module.sysmonitor.service.DeviceStateService;
import com.jec.module.sysmonitor.service.NetWorkMonitorService;
import com.jec.module.sysmonitor.service.NetWorkStateService;
import com.jec.module.sysmonitor.vo.CardConfig;
import com.jec.module.sysmonitor.vo.Device;
import com.jec.utils.Constants;
import com.jec.utils.Response;
import com.jec.utils.tool.StringHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by jeremyliu on 5/11/16.
 */
@Controller
@RequestMapping(value = "/network")
public class NetWorkMonitorController {

    @Resource
    private NetWorkMonitorService netWorkMonitorService;

    @Resource
    private NetWorkStateService netWorkStateService;

    @Resource
    private DeviceStateService deviceStateService;

    @RequestMapping(method = RequestMethod.POST, value = "netunit/create")
    public @ResponseBody
    Response createNetUnit(@RequestParam(value="name") String name,
                           @RequestParam(value="ip") String ip,
                           @RequestParam(value="slot", required = false) Integer slot){
        Response res = Response.Builder().status(Response.STATUS_PARAM_ERROR);
        if(name.equals("") || name.length() > 60){
            res.message("网元名字不合法");
            return res;
        }

        if(!StringHelper.checkIP(ip)){
            res.message("ip地址不合法");
            return res;
        }

        if(slot == null)
            slot = Constants.DefualtNetUnitSlot;
        if(slot <=0 || slot > 20){
            res.message("板卡数目不正确");
            return res;
        }

        int netUnitId =  netWorkMonitorService.addNetUnit(name, ip, Constants.DefualtNetUnitPort, slot);
        if(netUnitId < 0){
            if(netUnitId == -2)
                res.status(Response.STATUS_PARAM_ERROR).message("ip地址与其他网元存在冲突");
            else
                res.status(Response.STATUS_SYS_ERROR);
            return res;
        }
        res.status(Response.STATUS_SUCCESS).data(netUnitId);
        return res;
    }

    @RequestMapping(method = RequestMethod.POST, value = "netunit/modify")
    public @ResponseBody
    Response modifyNetUnit(@RequestParam(value="id") int id,
                           @RequestParam(value="name") String name,
                           @RequestParam(value="ip") String ip,
                           @RequestParam(value="slot", required = false) Integer slot){
        Response res = Response.Builder().status(Response.STATUS_PARAM_ERROR);
        if(name.equals("") || name.length() > 60){
            res.message("网元名字不合法");
            return res;
        }

        if(!StringHelper.checkIP(ip)){
            res.message("ip地址不合法");
            return res;
        }

        if(slot == null)
            slot = Constants.DefualtNetUnitSlot;
        if(slot <=0 || slot > 20){
            res.message("板卡数目不正确");
            return res;
        }

        int netUnit = netWorkMonitorService.modifyNetUnit(id, name,ip, Constants.DefualtNetUnitPort, slot);
        if(netUnit == -1){
            res.message("网元不存在");
            return res;
        }

        if(netUnit == -2){
            res.message("ip地址与其他网元冲突");
            return res;
        }

        res.status(Response.STATUS_SUCCESS).data(netUnit);
        return res;
    }


    @RequestMapping(method = RequestMethod.POST, value = "netunit/connect/create")
    public @ResponseBody
    Response netUnitconnect(@RequestParam(value="slot") int slot,
                            @RequestParam(value="port") int port,
                           @RequestParam(value="srcId") int srcId,
                            @RequestParam(value = "destId") int destId){
        Response res = Response.Builder().status(Response.STATUS_PARAM_ERROR);
        if( destId <= 0 || srcId <=0 || slot <0 || port <0)
            return res;
        boolean result = netWorkMonitorService.saveNetConnect(srcId,destId,slot,port);
        if(result)
            res.status(Response.STATUS_SUCCESS);
        return res;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/topo")
    public @ResponseBody
    Response getConfig(){
        return Response.Builder(netWorkMonitorService.getNetUnitAndDevice());
    }


    @RequestMapping(method = RequestMethod.GET, value = "/netunit/cardslot")
    public @ResponseBody
    Response getCardSlot(
            @RequestParam(value="netunit") int netunit,
            @RequestParam(value="type") int type){
        return Response.Builder(netWorkMonitorService.getNetUnitCardSlot(netunit,type));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/netunit")
    public @ResponseBody
    Response getNetUnit(@RequestParam(value="netunit", required = false) Integer netunit){
        if(netunit == null || netunit <= 0)
            return Response.Builder(netWorkMonitorService.getAllNetUnit());
        else
            return Response.Builder(netWorkMonitorService.getNetUnit(netunit));
    }

    @RequestMapping(method =RequestMethod.DELETE, value = "/netunit/remove")
    public @ResponseBody
    Response removeNetUnit(@RequestParam(value="id") int netUnitId){
        boolean result = netWorkMonitorService.removeNetUnit(netUnitId);
        return Response.Builder(result);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/card")
    public @ResponseBody
    Response getCard(@RequestParam(value="netUnit") int netUnitId){
        return Response.Builder(netWorkMonitorService.getCardByNetUnit(netUnitId));
    }

//    @RequestMapping(method = RequestMethod.POST, value = "/card/modify", consumes = "application/json;charset=UTF-8")
//    public @ResponseBody
//    Response modifyCard(@RequestBody List<CardConfig> cardConfig){
//
//        return Response.Builder().status(netWorkMonitorService.modifyCard(cardConfig)?
//                Response.STATUS_SUCCESS:Response.STATUS_SYS_ERROR);
//    }

    @RequestMapping(method = RequestMethod.POST, value = "/card/modify")
    public @ResponseBody
    Response modifyCard(@RequestParam(value="id") int cardId,
                        @RequestParam(value="type") int type){
        return Response.Builder().status(netWorkMonitorService.modifyCard(cardId,type)?
                Response.STATUS_SUCCESS:Response.STATUS_PARAM_ERROR);
    }

//    @RequestMapping(method = RequestMethod.GET, value = "/device")
//    public @ResponseBody
//    Response getAllDevice(@RequestParam(value="card", required = false) Integer cardId){
//        if(cardId == null)
//            return Response.Builder(netWorkMonitorService.getAllDevice());
//        else
//            return Response.Builder(netWorkMonitorService.getDeviceByCard(cardId));
//    }

    @RequestMapping(method = RequestMethod.GET, value = "/state")
    public @ResponseBody
    Response getNetworkState(@RequestParam(value="netunit", required = false) Integer netunit){
        if(netunit == null)
            return Response.Builder(netWorkStateService.readNetUnitState());
        else
            return Response.Builder(netWorkStateService.getInitState(netunit));
    }

    @RequestMapping(method = RequestMethod.GET, value = "/card/type")
    public @ResponseBody
    Response getCardType(){
        return Response.Builder(netWorkMonitorService.getCardTypeMap());
    }
}