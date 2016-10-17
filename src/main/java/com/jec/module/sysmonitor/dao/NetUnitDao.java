package com.jec.module.sysmonitor.dao;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.googlecode.genericdao.search.Search;
import com.jec.base.dao.BaseDao;
import com.jec.module.sysmonitor.entity.Card;
import com.jec.module.sysmonitor.entity.NetUnit;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jeremyliu on 5/10/16.
 */
@Repository
public class NetUnitDao extends BaseDao<NetUnit,Integer> implements GenericDAO<NetUnit,Integer> {

    public void updateIp(String ip, int id){
        int newId = NetUnit.getIdFromIp(ip);
        Map<String, Object> param = new HashMap<>();
        param.put("newId", newId);
        param.put("oldId", id);
        param.put("ip", ip);
        String sql = "update zhwg_monitor_element set id = :newId, ip = :ip where id = :oldId";
        updateSQLQuery(sql, param);
    }

    public NetUnit getNetUnitByNetId(int netId){
        Search search = new Search(NetUnit.class);
        search.addFilterEqual("netId", netId);
        return searchUnique(search);
    }
}
