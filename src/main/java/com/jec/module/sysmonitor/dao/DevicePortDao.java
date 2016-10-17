package com.jec.module.sysmonitor.dao;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.googlecode.genericdao.search.Search;
import com.jec.base.dao.BaseDao;
import com.jec.module.sysmonitor.entity.DevicePort;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jeremyliu on 6/5/16.
 */
@Repository
public class DevicePortDao extends BaseDao<DevicePort, Integer> implements GenericDAO<DevicePort, Integer>{

    public int updatePort(int id, String number, String function, boolean enable){

        Map<String, Object> param = new HashMap<>();
        if(number != null)
            param.put("number",number);
        if(function != null)
            param.put("function", function);
        param.put("enable", enable?1:0);
        String sql = buildUpdate(param.keySet());
        if(param.size() == 0)
            return 0;
        param.put("oldId", id);
        sql = "update zhwg_device_port set " + sql +" where id = :oldId";

        return this.updateSQLQuery(sql,param);
    }

    public boolean exist(DevicePort devicePort){
        Search search=new Search(DevicePort.class);
        search.addFilterNotEqual("id", devicePort.getId());
        search.addFilterEqual("deviceId", devicePort.getDeviceId());
        search.addFilterEqual("number", devicePort.getNumber());
        return searchUnique(search)!=null;
    }
}
