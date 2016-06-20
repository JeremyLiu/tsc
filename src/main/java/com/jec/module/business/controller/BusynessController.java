package com.jec.module.business.controller;

import com.jec.module.business.service.BusynessService;
import com.jec.utils.Response;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * Created by jeremyliu on 5/18/16.
 */
@Controller
@RequestMapping(value = "/business")
public class BusynessController {

    @Resource
    private BusynessService busynessService;

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
}
