package com.jec.module.sysconfig.service.convertor;

import com.jec.module.sysconfig.dao.TerminalKeyConfigDao;
import com.jec.module.sysconfig.entity.TerminalKeyConfig;
import com.jec.module.sysmonitor.dao.TerminalDeviceDao;
import com.jec.utils.Response;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by jeremyliu on 09/10/2016.
 */
@Service
public class TerminalKeyConvertor implements Convertor<TerminalKeyConfig> {

    @Resource
    private TerminalKeyConfigDao terminalKeyConfigDao;

    @Resource
    private TerminalDeviceDao terminalDeviceDao;

    @Override
    public Response process(TerminalKeyConfig terminalKeyConfig) {

        Response response = Response.Builder().status(Response.STATUS_PARAM_ERROR);
        if(terminalDeviceDao.findByCode(terminalKeyConfig.getDeviceNumber()) == null)
            return response.message("终端号码不存在");

        if(terminalKeyConfigDao.exist(terminalKeyConfig))
            return response.message("该终端该按键已配置");

        return response.status(Response.STATUS_SUCCESS);
    }

    @Override
    public Response checkBeforeRemove(int id) {
        return null;
    }
}
