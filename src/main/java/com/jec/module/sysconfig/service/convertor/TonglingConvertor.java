package com.jec.module.sysconfig.service.convertor;

import com.jec.module.sysconfig.dao.TonglingDao;
import com.jec.module.sysconfig.entity.TonglingConfig;
import com.jec.module.sysmonitor.dao.NetUnitDao;
import com.jec.utils.Response;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by jeremyliu on 9/26/16.
 */
@Service
public class TonglingConvertor implements Convertor<TonglingConfig> {

    @Resource
    private NetUnitDao netUnitDao;

    @Resource
    private TonglingDao tonglingDao;

    @Override
    public Response process(TonglingConfig tonglingConfig) {
        Response response = Response.Builder().status(Response.STATUS_PARAM_ERROR);

        if(netUnitDao.find(tonglingConfig.getNetunit()) == null)
            return response.message("网元不存在");
        if(tonglingDao.exist(tonglingConfig.getNetunit(), tonglingConfig.getCode()))
            return response.message("该网元编码为"+tonglingConfig.getCode()+"已配置");

        return response.status(Response.STATUS_SUCCESS);
    }
}
