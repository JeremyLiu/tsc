package com.jec.module.sysconfig.dao;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.googlecode.genericdao.search.Search;
import com.jec.base.dao.BaseDao;
import com.jec.module.sysconfig.entity.VdrConfig;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jeremyliu on 08/10/2016.
 */
@Repository
public class VdrDao extends BaseDao<VdrConfig, Integer> implements GenericDAO<VdrConfig, Integer>{

    public boolean exist(VdrConfig vdrConfig){
        Search search = new Search(VdrConfig.class);
        search.addFilterNotEqual("id", vdrConfig.getId());
        search.addFilterEqual("number", vdrConfig.getNumber());
        search.addFilterEqual("portNumber", vdrConfig.getPortNumber());
        return searchUnique(search)!=null;
    }

    public List<VdrConfig> findByNetUnit(int netUnitId){
        Search search = new Search(VdrConfig.class);
        search.addFilterEqual("netunit", netUnitId);
        return search(search);
    }

    public List<VdrConfig> findByDevice(String number){
        Search search = new Search(VdrConfig.class);
        search.addFilterEqual("deviceNumber", number);
        return search(search);
    }
}
