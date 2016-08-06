package com.jec.module.sysmanage.controller;

import com.jec.module.sysmanage.service.SysLogService;
import com.jec.utils.DateTimeUtils;
import com.jec.utils.Response;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.Date;

/**
 * Created by jeremyliu on 7/24/16.
 */
@Controller
@RequestMapping(value = "/log")
public class LogController {

    private static final int defaultPageSize = 10;

    @Resource
    private SysLogService sysLogService;

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    Response index(@RequestParam(value = "page", required = false, defaultValue = "1") int page,
                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
                   @RequestParam(value = "startDate", required = false, defaultValue = "") String startDateStr,
                   @RequestParam(value = "endDate", required = false, defaultValue = "") String endDateStr,
                   @RequestParam(value = "searchKey", required = false, defaultValue = "") String searchKey){
        Response resp = Response.Builder();
        if(page < 1)
            page = 1;
        if(pageSize <= 0)
            pageSize = defaultPageSize;
        int start = pageSize* (page-1);

        Date startDate = DateTimeUtils.String2Date(startDateStr);

        endDateStr += " 23:59:59";
        Date endDate = DateTimeUtils.String2DateTime(endDateStr);

        try {
            searchKey = new String(searchKey.getBytes("ISO-8859-1"), "UTF-8");
        }catch(Exception e){
            e.printStackTrace();
        }

        return resp.data(sysLogService.fetchLog(start, pageSize, startDate, endDate, searchKey));
    }

    @RequestMapping(method = RequestMethod.DELETE, consumes = "application/json;charset=UTF-8")
    public @ResponseBody
    Response removeLog(@RequestBody Integer[] ids){
        Response resp = Response.Builder();
        try{
            sysLogService.batchRemoveLog(ids);
            return resp;
        }catch (Exception e){
            return resp.status(Response.STATUS_SYS_ERROR).data(false).message(e.getMessage());
        }
    }
}
