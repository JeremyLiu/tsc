package com.jec.module.sysconfig.service.convertor;

import com.jec.module.sysconfig.dao.DigitTrunkDao;
import com.jec.module.sysconfig.entity.DigitTrunk;
import com.jec.module.sysmonitor.dao.CardViewDao;
import com.jec.module.sysmonitor.dao.NetUnitDao;
import com.jec.module.sysmonitor.entity.view.CardView;
import com.jec.utils.Response;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by jeremyliu on 9/26/16.
 */
@Service
public class DigitTrunkConvertor implements Convertor<DigitTrunk> {

    @Resource
    private NetUnitDao netUnitDao;

    @Resource
    private DigitTrunkDao digitTrunkDao;

    @Resource
    private CardViewDao cardViewDao;

    @Override
    @Transactional(readOnly = true)
    public Response process(DigitTrunk digitTrunk) {
        Response response = Response.Builder().status(Response.STATUS_PARAM_ERROR);

        if(netUnitDao.find(digitTrunk.getNetunit()) == null)
            return response.message("网元不存在");

        CardView card = cardViewDao.getCardBySlot(digitTrunk.getNetunit(), digitTrunk.getSlot());
        if(card == null)
            return response.message("主时钟槽位非法");
        if(card.getPortCount()<=digitTrunk.getPort())
            return response.message("主时钟端口非法");

        if(digitTrunkDao.exist(digitTrunk))
            return response.message("该网元的槽位端口的数字中继已配置");

        return response.status(Response.STATUS_SUCCESS);
    }

    @Override
    public Response checkBeforeRemove(int id) {
        return null;
    }
}
