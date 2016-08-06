package com.jec.module.business.controller;

import com.jec.module.business.entity.RecordSegment;
import com.jec.module.business.service.BusynessService;
import com.jec.module.business.service.RecordService;
import com.jec.utils.DateTimeUtils;
import com.jec.utils.DownloadUtils;
import com.jec.utils.Response;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
                       @RequestParam(value="startDate",required = false, defaultValue ="") String startDateStr,
                       @RequestParam(value="endDate", required = false, defaultValue = "") String endDateStr,
                       @RequestParam(value="search", required = false, defaultValue = "") String key,
                       @RequestParam(value="page", required = false, defaultValue = "1") Integer page,
                       @RequestParam(value="pageSize", required = false, defaultValue = "10") Integer pageSize){

        Response res = Response.Builder();

        Date startDate = DateTimeUtils.String2Date(startDateStr);

        endDateStr += " 23:59:59";
        Date endDate = DateTimeUtils.String2DateTime(endDateStr);
        if(page<=0)
            page=1;

        int offset = (page-1)*pageSize;

        if(key != null && key.length()>100){
            return res.status(Response.STATUS_PARAM_ERROR).message("搜索字符长度不能超过100");
        }
        try {
            key = new String(key.getBytes("ISO-8859-1"), "UTF-8");
        }catch(Exception e){
            e.printStackTrace();
        }
        if(state.equals("history"))
            return res.data(recordService.getRecord(startDate, endDate,key,offset, pageSize));
        else if(state.equals("current"))
            return res.data(recordService.getCurrentRecord());
        else{
            Map<String,Object> map = new HashMap<>();
            map.put("current",recordService.getCurrentRecord());
            map.put("history", recordService.getRecord(startDate, endDate, key,offset, pageSize));
            return res.data(map);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value="record/play")
    public void downloadRecord(HttpServletRequest request, HttpServletResponse response,
                               @RequestParam(value="id") Integer id,
                               @RequestParam(value="recording", required = false, defaultValue = "false") boolean recording){
        List<RecordSegment> recordSegmentList = recordService.getRecordSegment(id,recording);
        if(recordSegmentList == null)
            return;

        String[] files = new String[recordSegmentList.size()];
        for(int i=0;i<recordSegmentList.size();i++)
            files[i] = recordSegmentList.get(i).getTargetFile();

        DownloadUtils.downloadMp3(request, response, files);
    }

    @RequestMapping(method = RequestMethod.DELETE, value= "record/remove")
    public @ResponseBody
    Response removeRecord(@RequestParam(value="id") int id){
        return Response.Builder(recordService.removeRecord(id));
    }
}
