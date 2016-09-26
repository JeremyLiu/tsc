package com.jec.module.sysconfig.service;

import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.SearchResult;
import com.jec.base.dao.BaseDao;
import com.jec.base.entity.PageList;
import com.jec.module.sysconfig.dao.DigitTrunkDao;
import com.jec.module.sysconfig.entity.*;
import com.jec.module.sysconfig.service.convertor.Convertor;
import com.jec.module.sysmonitor.dao.NetUnitDao;
import com.jec.module.sysmonitor.entity.NetUnit;
import com.jec.utils.Response;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jeremyliu on 7/2/16.
 */
@Service
public class ConfigService implements ApplicationContextAware{

    @Resource
    private NetUnitDao netUnitDao;

    private Map<Class<?>, BaseDao> map = new HashMap<>();

    private Map<String, BaseDao> strMap = new HashMap<>();

    private Map<Class<? >, Convertor<NetUnitConfig>> convertorMap = new HashMap<>();

    private ApplicationContext context;

    @PostConstruct
    public void init(){
        map.put(Clock.class, (BaseDao)context.getBean("clockDao"));
        map.put(MeetingConfig.class, (BaseDao)context.getBean("meetingDao"));
        map.put(TonglingConfig.class, (BaseDao)context.getBean("tonglingDao"));
        map.put(DigitTrunk.class, (BaseDao)context.getBean("digitTrunkDao"));
        strMap.put("clock", (BaseDao)context.getBean("clockDao"));
        strMap.put("meeting", (BaseDao)context.getBean("meetingDao"));
        strMap.put("tongling", (BaseDao)context.getBean("tonglingDao"));
        strMap.put("digittrunk", (BaseDao)context.getBean("digitTrunkDao"));
        convertorMap.put(Clock.class, (Convertor<NetUnitConfig>) context.getBean("clockConvertor"));
        convertorMap.put(DigitTrunk.class, (Convertor<NetUnitConfig>) context.getBean("digitTrunkConvertor"));
        convertorMap.put(MeetingConfig.class, (Convertor<NetUnitConfig>) context.getBean("meetingConvertor"));
        convertorMap.put(TonglingConfig.class, (Convertor<NetUnitConfig>) context.getBean("tonglingConvertor"));
    }

    @Transactional
    public Response saveOrUpdate(NetUnitConfig config){
        Response response = config.validate();
        if((Integer)response.get("status") !=  Response.STATUS_SUCCESS)
            return response;
        Convertor<NetUnitConfig> convertor = convertorMap.get(config.getClass());
        if(convertor!=null){
            response = convertor.process(config);
            if((Integer)response.get("status") !=  Response.STATUS_SUCCESS)
                return response;
        }
        response = Response.Builder().status(Response.STATUS_PARAM_ERROR);
        NetUnit netUnit = netUnitDao.find(config.getNetunit());
        if(netUnit == null)
            return response.message("网元错误");

        BaseDao dao = map.get(config.getClass());
        if(dao == null)
            return response.message("类型错误");

        config.setUpdateDate();
        dao.save(config);

        return response.status(Response.STATUS_SUCCESS).data(config);
    }

    @Transactional
    public List<NetUnitConfig> getConfig(Class<? extends NetUnitConfig> clazz, int page, int pageSize){
        BaseDao dao = map.get(clazz);
        if(dao == null || page<=0 || pageSize <= 0)
            return null;
        int start = (page - 1) * pageSize;
        Search search = new Search(clazz);
        search.setFirstResult(start);
        search.setMaxResults(pageSize);
        return dao.search(search);
    }

    @Transactional
    public boolean removeConfig(String type,int id, int netunit){
        if(strMap.containsKey(type)){
            BaseDao dao = strMap.get(type);
            if(id>0)
                return dao.removeById(id);
            else
                return dao.removeById(netunit);
        }else
            return false;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
