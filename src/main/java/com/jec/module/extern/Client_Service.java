package com.jec.module.extern;

import com.jec.base.entity.MessageHead;
import com.jec.module.sysmonitor.entity.LostRate;
import com.jec.module.sysmonitor.entity.NEFaultInfo;
import com.jec.module.sysmonitor.entity.NmsHeartbeat;
import com.jec.module.sysmonitor.entity.PerformanceReport;
import com.jec.utils.XmlUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by jeremyliu on 5/24/16.
 */
@Service
public class Client_Service {

    Log log = LogFactory.getLog(Client_Service.class);

    @PostConstruct
    public void init(){
        NmsHeartbeat nmsHeartbeat = new NmsHeartbeat();
        String xml = XmlUtils.serviceMsg(MessageHead.MSG_TYPE_HB, nmsHeartbeat, new Class[]{NmsHeartbeat.class, NmsHeartbeat.NmsStatus.class});
        new Thread(){
            @Override
            public void run(){
                try{
                    Thread.sleep(5000);
                }catch(InterruptedException e){
                    log.error("send service shutdown");
                }
            }
        }.start();
    }

    public String reportInfo(List<NEFaultInfo> faultInfoList){
        return XmlUtils.serviceMsg(MessageHead.MSG_TYPE_ERROR,faultInfoList, new Class[]{NEFaultInfo.class},"NEFaultsReport");
    }

    public String reportPerformance(int rate){
        PerformanceReport performanceReport = new PerformanceReport(rate);
        return XmlUtils.serviceMsg(MessageHead.MSG_TYPE_HSL,performanceReport, new Class[]{PerformanceReport.class, LostRate.class});
    }

}
