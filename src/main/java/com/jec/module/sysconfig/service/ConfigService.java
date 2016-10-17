package com.jec.module.sysconfig.service;

import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.SearchResult;
import com.jec.base.dao.BaseDao;
import com.jec.base.entity.PageList;
import com.jec.module.business.entity.Broadcast;
import com.jec.module.sysconfig.dao.DigitTrunkDao;
import com.jec.module.sysconfig.entity.*;
import com.jec.module.sysconfig.service.convertor.Convertor;
import com.jec.module.sysmanage.service.SysLogService;
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

    @Resource
    private SysLogService sysLogService;

    public Map<Class<?>, BaseDao> map = new HashMap<>();

    private Map<String, BaseDao> strMap = new HashMap<>();

    private Map<Class<? >, Convertor<NetUnitConfig>> convertorMap = new HashMap<>();
    private final Map<Class<? extends NetUnitConfig>, String> classToChMap = new HashMap<Class<? extends NetUnitConfig>, String>(){{
        put(MeetingConfig.class,"会议");
        put(Clock.class, "时钟");
        put(TonglingConfig.class, "通令");
        put(NumberEntry.class,"用户数据");
        put(DigitTrunk.class,"数字中继");
        put(BroadcastConfig.class,"广播");
        put(PhoneStation.class,"话务台");
        put(TerminalBusiness.class,"终端业务");
        put(TerminalKeyConfig.class, "终端按键");
    }};
    private final Map<String, String> enToChMap = new HashMap<String, String>(){{
        put("meeting","会议");
        put("clock", "时钟");
        put("tongling", "通令");
        put("userdata","用户数据");
        put("digittrunk","数字中继");
        put("broadcast","广播");
        put("vdr","VDR");
        put("phonestation","话务台");
        put("terminalbusiness","终端业务");
        put("terminalkey", "终端按键");
    }};
    private ApplicationContext context;

    @PostConstruct
    public void init(){
        map.put(Clock.class, (BaseDao)context.getBean("clockDao"));
        map.put(MeetingConfig.class, (BaseDao)context.getBean("meetingDao"));
        map.put(TonglingConfig.class, (BaseDao)context.getBean("tonglingDao"));
        map.put(DigitTrunk.class, (BaseDao)context.getBean("digitTrunkDao"));
        map.put(NumberEntry.class, (BaseDao)context.getBean("numberEntryDao"));
        map.put(BroadcastConfig.class, (BaseDao)context.getBean("broadcastDao"));
        map.put(PhoneStation.class, (BaseDao)context.getBean("phoneStationDao"));
        map.put(TerminalBusiness.class, (BaseDao)context.getBean("terminalBusinessDao"));
        map.put(TerminalKeyConfig.class, (BaseDao)context.getBean("terminalKeyConfigDao"));
        strMap.put("clock", (BaseDao)context.getBean("clockDao"));
        strMap.put("meeting", (BaseDao)context.getBean("meetingDao"));
        strMap.put("tongling", (BaseDao)context.getBean("tonglingDao"));
        strMap.put("digittrunk", (BaseDao)context.getBean("digitTrunkDao"));
        strMap.put("userdata", (BaseDao)context.getBean("numberEntryDao"));
        strMap.put("broadcast",(BaseDao)context.getBean("broadcastDao"));
        strMap.put("phonestation", (BaseDao)context.getBean("phoneStationDao"));
        strMap.put("terminalbusiness", (BaseDao)context.getBean("terminalBusinessDao"));
        strMap.put("terminalkey", (BaseDao)context.getBean("terminalKeyConfigDao"));
        convertorMap.put(Clock.class, (Convertor<NetUnitConfig>) context.getBean("clockConvertor"));
        convertorMap.put(DigitTrunk.class, (Convertor<NetUnitConfig>) context.getBean("digitTrunkConvertor"));
        convertorMap.put(MeetingConfig.class, (Convertor<NetUnitConfig>) context.getBean("meetingConvertor"));
        convertorMap.put(TonglingConfig.class, (Convertor<NetUnitConfig>) context.getBean("tonglingConvertor"));
        convertorMap.put(NumberEntry.class, (Convertor<NetUnitConfig>) context.getBean("numberEntryConvertor"));
        convertorMap.put(BroadcastConfig.class, (Convertor<NetUnitConfig>) context.getBean("broadcastConvertor"));
        convertorMap.put(PhoneStation.class, (Convertor<NetUnitConfig>) context.getBean("phoneStationConvertor"));
        convertorMap.put(TerminalBusiness.class, (Convertor<NetUnitConfig>) context.getBean("terminalBusinessConvertor"));
        convertorMap.put(TerminalKeyConfig.class, (Convertor<NetUnitConfig>) context.getBean("terminalKeyConvertor"));
    }

    @Transactional
    public Response saveOrUpdate(NetUnitConfig config){
        Response response = config.validate();
        if(response!= null && (Integer)response.get("status") !=  Response.STATUS_SUCCESS)
            return response;
        Convertor<NetUnitConfig> convertor = convertorMap.get(config.getClass());
        if(convertor!=null){
            response = convertor.process(config);
            if((Integer)response.get("status") !=  Response.STATUS_SUCCESS)
                return response;
        }
        response = Response.Builder().status(Response.STATUS_PARAM_ERROR);
        if(config.getNetunit()!=-1) {
            NetUnit netUnit = netUnitDao.find(config.getNetunit());
            if (netUnit == null)
                return response.message("网元错误");
        }

        BaseDao dao = map.get(config.getClass());
        if(dao == null)
            return response.message("类型错误");

        config.setUpdateDate();
        boolean create = dao.save(config);

        String desc= create?"添加":"修改";
        if(config.getNetunit() != -1) {
            String name = netUnitDao.find(config.getNetunit()).getName();
            desc += name + "的";
        }
        desc += classToChMap.get(config.getClass()) + "配置";
        sysLogService.addLog(desc);

        return response.status(Response.STATUS_SUCCESS).data(config);
    }

    @Transactional
    public SearchResult getConfig(Class<? extends NetUnitConfig> clazz, int page, int pageSize){
        BaseDao dao = map.get(clazz);
        if(dao == null || page<=0 || pageSize <= 0)
            return null;
        int start = (page - 1) * pageSize;
        Search search = new Search(clazz);
        search.setFirstResult(start);
        search.setMaxResults(pageSize);
        return dao.searchAndCount(search);
    }

    @Transactional
    public boolean removeConfig(String type,int id, int netunit){
        if(strMap.containsKey(type)){
            BaseDao dao = strMap.get(type);
            boolean result;
            String name="";
            try {
                if (id > 0) {
                    NetUnitConfig config = (NetUnitConfig) dao.find(id);
                    name = netUnitDao.find(config.getNetunit()).getName();
                    result = dao.removeById(id);
                } else {
                    name = netUnitDao.find(netunit).getName();
                    result = dao.removeById(netunit);

                }
                if (result)
                    sysLogService.addLog("删除"+name+"的"+enToChMap.get(type)+"配置");
                return result;
            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }else
            return false;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
