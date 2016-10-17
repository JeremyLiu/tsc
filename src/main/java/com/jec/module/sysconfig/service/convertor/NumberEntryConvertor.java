package com.jec.module.sysconfig.service.convertor;

import com.jec.module.sysconfig.dao.NumberEntryDao;
import com.jec.module.sysconfig.entity.NumberEntry;
import com.jec.module.sysmonitor.dao.CardViewDao;
import com.jec.module.sysmonitor.dao.NetUnitDao;
import com.jec.module.sysmonitor.entity.view.CardView;
import com.jec.protocol.pdu.PduConstants;
import com.jec.utils.Response;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by jeremyliu on 05/10/2016.
 */
@Service
public class NumberEntryConvertor implements Convertor<NumberEntry> {
    @Resource
    private NetUnitDao netUnitDao;

    @Resource
    private NumberEntryDao numberEntryDao;

    @Resource
    private CardViewDao cardViewDao;

    @Override
    public Response process(NumberEntry numberEntry) {
        Response response = Response.Builder().status(Response.STATUS_PARAM_ERROR);

        if(netUnitDao.find(numberEntry.getNetunit()) == null)
            return response.message("网元不存在");

        if(numberEntryDao.exist(numberEntry))
            return response.message("该网元的槽位和时隙已经配置");

        if(numberEntryDao.exist(numberEntry.getId(), numberEntry.getNumber()))
            return response.message("用户号码重复");

        CardView card = cardViewDao.getCardBySlot(numberEntry.getNetunit(), numberEntry.getSlot());

        if(card == null || !PduConstants.userDataTsMap.containsKey(card.getCode()))
            return response.message("槽位不合法");

        if(PduConstants.userDataTsMap.get(card.getCode()) <= numberEntry.getTs())
            return response.message("时隙不合法");

        return response.status(Response.STATUS_SUCCESS);
    }

    @Override
    public Response checkBeforeRemove(int id) {
        return null;
    }
}
