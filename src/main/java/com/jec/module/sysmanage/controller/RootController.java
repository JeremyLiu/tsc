package com.jec.module.sysmanage.controller;

import com.jec.module.extern.Client_Service;
import com.jec.module.extern.impl.NTNMS_ServiceImpl;
import com.jec.module.sysmanage.entity.Role;
import com.jec.module.sysmanage.service.UserService;
import com.jec.module.sysmonitor.entity.NEFaultInfo;
import com.jec.utils.Response;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.xml.ws.Endpoint;
import java.util.List;

/**
 * Created by jeremyliu on 5/8/16.
 */
@Controller
@RequestMapping(value = "/")
public class RootController {

    @Resource
    private NTNMS_ServiceImpl testService;

    @Resource
    private Client_Service client_service;

    @Resource
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String main(HttpSession httpSession, Model model){
        Integer userId = (Integer)httpSession.getAttribute("userId");
        if(userId == null)
            return "login";
        else {
            Role role = userService.getUserRole(userId);
            model.addAttribute("role", role.toValue());
            return "index";
        }
    }

    @RequestMapping(method = RequestMethod.GET ,value = "test")
    public String test(){
//        TestService service = new NTNMS_ServiceImpl();
        Endpoint.publish("http://localhost:8080/test",testService);
        return "";
    }

    @RequestMapping(method = RequestMethod.POST, value = "test/fault", consumes = "application/json;charset=UTF-8")
    public @ResponseBody
    Response reportFault(@RequestBody List<NEFaultInfo> faultInfoList){
        return Response.Builder(client_service.reportInfo(faultInfoList));
    }

    @RequestMapping(method = RequestMethod.GET, value = "test/topo")
    public @ResponseBody
    Response topo(){
        return Response.Builder(testService.topoQuery());
    }
}
