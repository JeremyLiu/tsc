package com.jec.module.sysconfig.service;

import com.jec.module.sysmonitor.dao.CardDao;
import com.jec.module.sysmonitor.dao.NetUnitDao;
import com.jec.module.sysmonitor.entity.Card;
import com.jec.module.sysmonitor.service.NetWorkStateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by jeremyliu on 7/2/16.
 */
@Service
public class ConfigService {

    @Resource
    private CardDao cardDao;

    @Resource
    private NetUnitDao netUnitDao;


    @Transactional
    public boolean downloadCard(int netUnit){
        List<Card> cards = cardDao.getCardByNetunit(netUnit);

        if(cards.size() == 0)
            return false;

        Card mainCard = cardDao.getMainCard(netUnit);
        if(mainCard == null)
            return false;

        return true;
    }
}
