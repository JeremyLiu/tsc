package com.jec.module.sysconfig.controller;

import com.jec.module.sysconfig.dao.NumberEntryDao;
import com.jec.module.sysconfig.entity.*;
import com.jec.module.sysconfig.service.*;
import com.jec.module.sysconfig.service.convertor.ClockConvertor;
import com.jec.module.sysmanage.service.SysLogService;
import com.jec.module.sysmonitor.dao.NetUnitDao;
import com.jec.utils.Response;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jeremyliu on 7/4/16.
 */
@Controller
@RequestMapping(value = "/config")
public class ConfigController implements ApplicationContextAware{

    @Resource
    private ConfigService configService;

    @Resource
    private TimeDownloadService timeDownloadService;

    @Resource
    private NumberEntryDao numberEntryDao;

    private Map<String, DownloadService> downloadServices = new HashMap<>();

    private Map<String, Class<? extends NetUnitConfig>> strMap = new HashMap<>();

    private Map<String, String> enToChMap = new HashMap<String, String>(){{
        put("card", "板卡");
        put("meeting","会议");
        put("clock", "时钟");
        put("tongling", "通令");
        put("userdata","用户数据");
        put("digittrunk","数字中继");
        put("broadcast","广播");
    }};

    private ApplicationContext context;

    @PostConstruct
    public void init(){
        downloadServices.put("card", (DownloadService)context.getBean("cardDownloadService"));
        downloadServices.put("clock", (DownloadService)context.getBean("clockDownloadService"));
        downloadServices.put("meeting", (DownloadService)context.getBean("meetingDownloadService"));
        downloadServices.put("tongling", (DownloadService)context.getBean("tonglingDownloadService"));
        downloadServices.put("digittrunk", (DownloadService)context.getBean("digitTrunkDownloadService"));
        downloadServices.put("userdata", (DownloadService)context.getBean("numberEntryDownloadService"));
        downloadServices.put("broadcast", (DownloadService)context.getBean("broadcastDownloadService"));
        downloadServices.put("phonestation", (DownloadService)context.getBean("phoneStationDownloadService"));
        downloadServices.put("vdr", (DownloadService)context.getBean("vdrDownloadService"));
        downloadServices.put("terminalbusiness", (DownloadService)context.getBean("terminalBusinessDownloadService"));
        downloadServices.put("terminalkey", (DownloadService)context.getBean("terminalKeyDownloadService"));
        strMap.put("clock", Clock.class);
        strMap.put("meeting", MeetingConfig.class);
        strMap.put("tongling", TonglingConfig.class);
        strMap.put("digittrunk", DigitTrunk.class);
        strMap.put("userdata", NumberEntry.class);
        strMap.put("broadcast", BroadcastConfig.class);
        strMap.put("phonestation", PhoneStation.class);
    }

    @RequestMapping(value = "download/{type}", method = RequestMethod.POST)
    public
    @ResponseBody
    Response download(@PathVariable(value = "type") String type,
                     @RequestParam(value = "netunit", required = false, defaultValue = "0") int netunitId){
        DownloadService downloadService = downloadServices.get(type);
        if(downloadService == null)
            return Response.Builder().status(Response.STATUS_PARAM_ERROR);
        if(netunitId > 0)
            return downloadService.download(netunitId);
        else
            return downloadService.download();
    }

    @RequestMapping(value = "download/device/{type}", method = RequestMethod.POST)
    public
    @ResponseBody
    Response downloadDevice(@PathVariable(value = "type") String type,
                            @RequestParam(value = "device", required = false, defaultValue = "") String deviceNumber,
                      @RequestParam(value = "netunit", required = false, defaultValue = "0") int netunitId){
        DeviceDownloadService downloadService = (DeviceDownloadService)downloadServices.get(type);
        if(downloadService == null)
            return Response.Builder().status(Response.STATUS_PARAM_ERROR);
        if(!deviceNumber.equals("")){
            return downloadService.download(deviceNumber);
        }else if(netunitId>0)
            return downloadService.download(netunitId);
        else
            return downloadService.download();
    }

    @RequestMapping(value = "download/time", method = RequestMethod.POST)
    public
    @ResponseBody
    Response download(@RequestParam(value = "netunit", required = false, defaultValue = "0") int netunitId,
                      @RequestParam(value = "time", required = false, defaultValue = "-1") long ts){
        if(netunitId > 0)
            return timeDownloadService.download(netunitId, ts);
        else
            return timeDownloadService.download(ts);
    }

    @RequestMapping(value = "save/digittrunk", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
    public
    @ResponseBody
    Response digittrunk(@RequestBody DigitTrunk digittrunk){
        return configService.saveOrUpdate(digittrunk);
    }

    @RequestMapping(value = "save/meeting", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
    public
    @ResponseBody
    Response meeting(@RequestBody MeetingConfig meetingConfig){
        return configService.saveOrUpdate(meetingConfig);
    }

    @RequestMapping(value = "save/clock", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
    public @ResponseBody
    Response clock(@RequestBody Clock clockConfig){
       return configService.saveOrUpdate(clockConfig);
    }

    @RequestMapping(value = "save/tongling", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
    public @ResponseBody
    Response tongling(@RequestBody TonglingConfig tonglingConfig){
        return configService.saveOrUpdate(tonglingConfig);
    }

    @RequestMapping(value = "save/userdata", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
    public @ResponseBody
    Response userdata(@RequestBody NumberEntry numberEntry){
        return configService.saveOrUpdate(numberEntry);
    }

    @RequestMapping(value = "save/broadcast", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
    public @ResponseBody
    Response broadcast(@RequestBody BroadcastConfig broadcastConfig){
        return configService.saveOrUpdate(broadcastConfig);
    }

    @RequestMapping(value = "save/phonestation", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
    public @ResponseBody
    Response phonestation(@RequestBody PhoneStation phoneStation){
        return configService.saveOrUpdate(phoneStation);
    }

    @RequestMapping(value = "save/terminalbusiness", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
    public @ResponseBody
    Response terminalBusiness(@RequestBody TerminalBusiness terminalBusiness){
        return configService.saveOrUpdate(terminalBusiness);
    }

    @RequestMapping(value = "save/terminalkey", method = RequestMethod.POST, consumes = "application/json;charset=UTF-8")
    public @ResponseBody
    Response terminalKey(@RequestBody TerminalKeyConfig terminalKeyConfig){
        return configService.saveOrUpdate(terminalKeyConfig);
    }

    @RequestMapping(value = "remove/{type}", method = RequestMethod.DELETE)
    @ResponseBody
    Response removeConfig(@PathVariable(value = "type") String type,
            @RequestParam(value="netunit", required = false, defaultValue = "-1") int netunit,
                          @RequestParam(value="id", required = false, defaultValue = "-1") int id){
        return Response.Builder(configService.removeConfig(type, id, netunit));
    }

    @RequestMapping(value = "{type}", method = RequestMethod.GET)
    public @ResponseBody
    Response index(@RequestParam(value="page", required = false, defaultValue = "1") int page,
                   @RequestParam(value="pagesize", required = false, defaultValue = "20") int pageSize,
                   @PathVariable(value="type") String type){
        if(page <= 0)
            page = 1;
        if(pageSize <= 0)
            pageSize = 10;
        Response response =  Response.Builder();
        Class<? extends NetUnitConfig> clazz = strMap.get(type);
        if(clazz == null)
            return response.status(Response.STATUS_PARAM_ERROR);
        else
            return response.data(configService.getConfig(clazz,page, pageSize));
    }

    @RequestMapping(value = "numberentry", method = RequestMethod.GET)
    public @ResponseBody
    Response getNumberEnties(){
        return Response.Builder(numberEntryDao.getAllNumbers());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
