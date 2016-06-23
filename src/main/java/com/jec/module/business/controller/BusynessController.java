package com.jec.module.business.controller;

import com.jec.module.business.service.BusynessService;
import com.jec.module.business.service.RecordService;
import com.jec.utils.DateTimeUtils;
import com.jec.utils.Response;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jeremyliu on 5/18/16.
 */
@Controller
@RequestMapping(value = "/business")
public class BusynessController {

    private  final static int pageSize = 10;

    @Resource
    private BusynessService busynessService;

    @Resource
    private RecordService recordService;

    @RequestMapping(method = RequestMethod.GET, value="list")
    public @ResponseBody
    Response getBusinessList(@RequestParam(value="type") int type){
        return Response.Builder(busynessService.getList(type));
    }

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    Response index(){
        return Response.Builder(busynessService.getBrief());
    }

    @RequestMapping(method = RequestMethod.GET, value="record/{state}")
    public @ResponseBody
    Response getRecord(@PathVariable(value = "state") String state,
                       @RequestParam(value="startDate",required = false) String startDate,
                       @RequestParam(value="endDate", required = false) String endDate,
                       @RequestParam(value="search", required = false) String key,
                       @RequestParam(value="page", required = false) Integer page){

        Response res = Response.Builder();
        Long startTime = null;
        if(startDate != null && !startDate.equals("")){
            startDate += " 00:00:00";
            startTime=DateTimeUtils.String2TimeStamp(startDate);
        }
        Long endTime = null;
        if(endDate != null && !endDate.equals("")){
            endDate += " 23:59:59";
            endTime=DateTimeUtils.String2TimeStamp(endDate);
        }
        if(page == null || page<=0)
            page=1;
        int offset = (page-1)*pageSize;

        if(key != null && key.length()>100){
            return res.status(Response.STATUS_PARAM_ERROR).message("搜索字符长度不能超过100");
        }
        if(state.equals("history"))
            return res.data(recordService.getRecord(startTime, endTime,key,offset, pageSize));
        else if(state.equals("current"))
            return res.data(recordService.getCurrentRecord());
        else{
            Map<String,Object> map = new HashMap<>();
            map.put("current",recordService.getCurrentRecord());
            map.put("history", recordService.getRecord(startTime, endTime,key,offset, pageSize));
            return res.data(map);
        }
    }
}
