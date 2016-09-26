package com.jec.module.sysconfig.service.convertor;

import com.jec.module.sysconfig.entity.Clock;
import com.jec.module.sysmonitor.dao.CardDao;
import com.jec.module.sysmonitor.dao.CardViewDao;
import com.jec.module.sysmonitor.dao.NetUnitDao;
import com.jec.module.sysmonitor.entity.Card;
import com.jec.module.sysmonitor.entity.view.CardView;
import com.jec.utils.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by jeremyliu on 9/22/16.
 */
@Service
public class ClockConvertor implements Convertor<Clock>{

    @Resource
    private CardViewDao cardViewDao;

    @Resource
    private NetUnitDao netUnitDao;

    @Override
    @Transactional(readOnly = true)
    public Response process(Clock clock) {

        Response response = Response.Builder().status(Response.STATUS_PARAM_ERROR);

        if(netUnitDao.find(clock.getNetunit()) == null)
            return response.message("网元不存在");

        CardView card = cardViewDao.getCardBySlot(clock.getNetunit(), clock.getSlot1());
        if(card == null)
            return response.message("主时钟槽位非法");
        if(card.getPortCount()<=clock.getPort1())
            return response.message("主时钟端口非法");
        clock.setType1(card.getCode());

        card = cardViewDao.getCardBySlot(clock.getNetunit(), clock.getSlot2());
        if(card == null)
            return response.message("备时钟1槽位非法");
        if(card.getPortCount()<=clock.getPort2())
            return response.message("备时钟1端口非法");
        clock.setType2(card.getCode());

        card = cardViewDao.getCardBySlot(clock.getNetunit(), clock.getSlot3());
        if(card == null)
            return response.message("备时钟2槽位非法");
        if(card.getPortCount()<=clock.getPort3())
            return response.message("备时钟2端口非法");
        clock.setType3(card.getCode());
        return response.status(Response.STATUS_SUCCESS);
    }
}
