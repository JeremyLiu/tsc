package com.jec.module.extern.impl;

import com.jec.base.entity.MessageHead;
import com.jec.base.entity.XmlMessage;
import com.jec.module.extern.NTNMS_Service;
import com.jec.module.sysmonitor.entity.ConnectState;
import com.jec.module.sysmonitor.entity.LostRate;
import com.jec.module.sysmonitor.entity.PerformanceReport;
import com.jec.module.sysmonitor.service.NetWorkStateService;
import com.jec.utils.XmlUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.wsdl.Message;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by jeremyliu on 5/17/16.
 */
@WebService
@Service
@Scope
public class NTNMS_ServiceImpl implements NTNMS_Service, ApplicationContextAware {

    @Resource
    private NetWorkStateService netWorkStateService;

    @Override
    @WebMethod
    public String QueryRemoteRequest(String msg) {
        XmlMessage entity = (XmlMessage) XmlUtils.toEntity(msg,new Class<?>[]{XmlMessage.class, MessageHead.class});

        String msgType = entity.getMessageHead().getMessageType();

        if(msgType.equals(MessageHead.MSG_TYPE_TPQ))
            return topoQuery();
        else if(msgType.equals(MessageHead.MSG_TYPE_HSL))
            return reportPerformance(0);
        return msg;
    }

    private List<ConnectState> NETopoQuery() {
//        ApplicationContext ac = new ClassPathXmlApplicationContext("applicationContext.xml");
//        netWorkStateService = getApplicationContext().getBean(NetWorkStateService.class);
        return (List<ConnectState>) netWorkStateService.readNetUnitState().get("connet");
    }

    public String topoQuery(){
        List<ConnectState> connectStates = this.NETopoQuery();
        return XmlUtils.serviceMsg(MessageHead.MSG_TYPE_TPQ, connectStates,new Class[]{ConnectState.class},"NETopoReport");
    }

    private String reportPerformance(int rate){
        PerformanceReport performanceReport = new PerformanceReport(rate);
        return XmlUtils.serviceMsg(MessageHead.MSG_TYPE_HSL,performanceReport, new Class[]{PerformanceReport.class, LostRate.class});
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        String beans[] = applicationContext.getBeanDefinitionNames();
    }

//    private String queryTopo(){
//        Map<String, Object > map = netWorkStateService.readNetUnitState();
//
//    }
}
