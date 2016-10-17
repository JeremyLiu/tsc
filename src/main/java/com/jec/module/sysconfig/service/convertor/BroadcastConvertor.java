package com.jec.module.sysconfig.service.convertor;

import com.jec.module.sysconfig.dao.BroadcastDao;
import com.jec.module.sysconfig.entity.BroadcastConfig;
import com.jec.module.sysmonitor.dao.NetUnitDao;
import com.jec.utils.Response;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by jeremyliu on 05/10/2016.
 */
@Service
public class BroadcastConvertor implements Convertor<BroadcastConfig> {

    @Resource
    private NetUnitDao netUnitDao;

    @Resource
    private BroadcastDao broadcastDao;

    @Override
    public Response process(BroadcastConfig broadcastConfig) {
        Response response = Response.Builder().status(Response.STATUS_PARAM_ERROR);

        if(netUnitDao.find(broadcastConfig.getNetunit()) == null)
            return response.message("网元不存在");
        if(broadcastDao.exist(broadcastConfig.getBroadcaster()))
            return response.message("广播机号码重复");
        if(broadcastDao.exist(broadcastConfig.getNetunit(), broadcastConfig.getCode()))
            return response.message("该网元业务为"+broadcastConfig.getCode()+"已配置");

        return response.status(Response.STATUS_SUCCESS);
    }

    @Override
    public Response checkBeforeRemove(int id) {
        return null;
    }
}
