package com.jec.module.sysmonitor.dao;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import com.jec.base.dao.BaseDao;
import com.jec.module.sysmonitor.entity.NetConnect;
import com.jec.module.sysmonitor.entity.NetConnectId;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jeremyliu on 5/20/16.
 */
@Repository
public class NetConnectDao extends BaseDao<NetConnect, NetConnectId> implements GenericDAO<NetConnect, NetConnectId> {

    public void updateId(int oldId, int newId){
//        Search search = new Search(NetConnect.class);
//        search.addFilterEqual("srcId", oldId);
//        List<NetConnect> connects = this.search(search);
//        for(NetConnect connect: connects)
//            connect.setSrcId(newId);
//
//        search.clearFilters();
//        search.addFilterEqual("destId", oldId);
//        connects = this.search(search);
//        for(NetConnect connect: connects)
//            connect.setDestId(newId);

        Map<String, Object> param= new HashMap<>();
        param.put("oldId", oldId);
        param.put("newId", newId);

        String sql = "update zhwg_element_connect set src_id = :newId where src_id = :oldId";
        updateSQLQuery(sql,param);
        sql = "update zhwg_element_connect set dest_id = :newId where dest_id = :oldId";
        updateSQLQuery(sql,param);
    }

    //删除连接到目的网元的端口集合中的连接信息
    public int removeConnect(int dest, List<Integer> slots){

        if(slots.size() == 0)
            return 0;

        Search search = new Search(NetConnect.class);
        search.addFilterEqual("destId", dest);
        search.addFilterIn("slot", slots);
        List<NetConnect> netConnects = this.search(search);
        NetConnect[] array = new NetConnect[netConnects.size()];
        array = netConnects.toArray(array);
        this.remove(array);
        return netConnects.size();
    }

    public int removeConnect(int netUnit){
        Search search = new Search(NetConnect.class);
        Filter filter = Filter.or(new Filter("srcId", netUnit), new Filter("destId", netUnit));
        search.addFilter(filter);
        List<NetConnect> netConnects = this.search(search);
        this.remove(netConnects.toArray(new NetConnect[netConnects.size()]));
        return netConnects.size();
    }
}
