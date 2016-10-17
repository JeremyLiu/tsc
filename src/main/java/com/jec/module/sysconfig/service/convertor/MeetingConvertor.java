package com.jec.module.sysconfig.service.convertor;

import com.jec.module.business.entity.Meeting;
import com.jec.module.sysconfig.dao.MeetingDao;
import com.jec.module.sysconfig.entity.MeetingConfig;
import com.jec.module.sysmonitor.dao.NetUnitDao;
import com.jec.utils.Response;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by jeremyliu on 9/26/16.
 */
@Service
public class MeetingConvertor implements Convertor<MeetingConfig>{

    @Resource
    private NetUnitDao netUnitDao;

    @Resource
    private MeetingDao meetingDao;

    @Override
    public Response process(MeetingConfig meeting) {
        Response response = Response.Builder().status(Response.STATUS_PARAM_ERROR);

        if(netUnitDao.find(meeting.getNetunit()) == null)
            return response.message("网元不存在");
        if(meetingDao.exist(meeting))
            return response.message("该网元业务为"+meeting.getCode()+"已配置");

        return response.status(Response.STATUS_SUCCESS);
    }

    @Override
    public Response checkBeforeRemove(int id) {
        return null;
    }
}
