package com.jec.module.sysconfig.service.convertor;

import com.jec.module.sysconfig.dao.PhoneStationDao;
import com.jec.module.sysconfig.entity.PhoneStation;
import com.jec.module.sysmonitor.dao.NetUnitDao;
import com.jec.utils.Response;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by jeremyliu on 08/10/2016.
 */
@Service
public class PhoneStationConvertor implements Convertor<PhoneStation>{

    @Resource
    private NetUnitDao netUnitDao;

    @Resource
    private PhoneStationDao phoneStationDao;

    @Override
    public Response process(PhoneStation phonestation) {
        Response response = Response.Builder().status(Response.STATUS_PARAM_ERROR);

        if(netUnitDao.find(phonestation.getNetunit()) == null)
            return response.message("网元不存在");
        if(phoneStationDao.exist(phonestation))
            return response.message("该网元业务为"+phonestation.getCode()+"已配置");

        return response.status(Response.STATUS_SUCCESS);
    }

    @Override
    public Response checkBeforeRemove(int id) {
        return null;
    }
}
