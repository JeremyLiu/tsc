package com.jec.module.sysconfig.service.convertor;

import com.jec.module.sysconfig.dao.NumberEntryDao;
import com.jec.module.sysconfig.dao.TerminalBusinessDao;
import com.jec.module.sysconfig.dao.TerminalKeyConfigDao;
import com.jec.module.sysconfig.entity.TerminalBusiness;
import com.jec.module.sysmonitor.dao.TerminalDeviceDao;
import com.jec.utils.Response;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by jeremyliu on 09/10/2016.
 */
@Service
public class TerminalBusinessConvertor implements Convertor<TerminalBusiness> {

    @Resource
    private TerminalBusinessDao terminalBusinessDao;

    @Resource
    private TerminalDeviceDao terminalDeviceDao;

    @Resource
    private TerminalKeyConfigDao terminalKeyConfigDao;

    @Resource
    private NumberEntryDao numberEntryDao;

    @Override
    public Response process(TerminalBusiness terminalBusiness) {
        Response response = Response.Builder().status(Response.STATUS_PARAM_ERROR);
        if(terminalDeviceDao.findByCode(terminalBusiness.getDeviceNumber()) == null)
            return response.message("终端号码不存在");

        if(terminalBusinessDao.exist(terminalBusiness))
            return response.message("终端该业务已配置");

        if(terminalKeyConfigDao.find(terminalBusiness.getKeyConfigId())==null)
            return response.message("终端按键未配置");

        String[] members = terminalBusiness.getMemberList();
        if(numberEntryDao.findNumbers(members).size() != members.length)
            return response.message("成员号码不合法");
        return response.status(Response.STATUS_SUCCESS);
    }

    @Override
    public Response checkBeforeRemove(int id) {
        return null;
    }
}
