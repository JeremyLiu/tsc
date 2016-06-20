package com.jec.module.business.service;

import com.jec.module.business.entity.Business;
import com.jec.module.business.entity.Meeting;
import com.jec.module.business.entity.ShoreLine;
import com.jec.module.business.entity.Threetalk;
import com.jec.module.business.manage.*;
import com.jec.module.sysmonitor.service.NetWorkListenerService;
import com.jec.protocol.pdu.PDU;
import com.jec.protocol.pdu.PduConstants;
import com.jec.protocol.pdu.ProtocolUtils;
import com.jec.protocol.processor.Processor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jeremyliu on 5/18/16.
 */
@Service
@Scope
public class BusynessService implements Processor{

    private Map<Integer,BusinessManager> businessManagers = new HashMap<>();

    @Resource
    private NetWorkListenerService netWorkListenerService;

    @PostConstruct
    public void init(){
        businessManagers.put(PduConstants.MONITOR_BUSINESS_BROADCAST, new BroadcastManager());
        businessManagers.put(PduConstants.MONITOR_BUSINESS_MEETING, new MeetManager());
        businessManagers.put(PduConstants.MONITOR_BUSINESS_THREETALK, new ThreeTalkManager());
        businessManagers.put(PduConstants.MONITOR_BUSINESS_TONGLING, new TonglingManager());
        businessManagers.put(PduConstants.MONITOR_BUSINESS_P2PTALK, new TwoTalkManager());
        businessManagers.put(PduConstants.MONITOR_BUSINESS_DIGITALTRUNK, new ShoreLineManager(ShoreLine.TYPE_DR));
        businessManagers.put(PduConstants.MONITOR_BUSINESS_SIMULATETRUNK, new ShoreLineManager(ShoreLine.TYPE_SR));
        businessManagers.put(PduConstants.MONITOR_BUSINESS_VDRRECORD, new VdrManager());

        netWorkListenerService.addProcessor(PduConstants.CMD_TYPE_YWJS, this);
    }

    @Override
    public boolean process(PDU pdu) {
        int type = ProtocolUtils.getCmdConfig(pdu);
        BusinessManager manager = businessManagers.get(type);
        if(manager == null)
            return false;
        else{
            manager.processPdu(pdu);
            return true;
        }
    }

    public List getList(int type){
        BusinessManager businessManager = businessManagers.get(type);
        if( businessManager == null)
            return new ArrayList<>();
        else
            return businessManager.getEntries();
    }

    public List<Business> getBrief(){
        List<Business> businesses = new ArrayList<>();
        for(BusinessManager businessManager: businessManagers.values()){
            if(businessManager.getEntries().size()>0)
                businesses.add(businessManager.getBrief());

        }
        return businesses;
    }

    public void clearData(int netunit){
        for(BusinessManager businessManager: businessManagers.values()){
            businessManager.clear(netunit);
        }
    }
}
