package com.jec.module.sysconfig.dao;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.googlecode.genericdao.search.Search;
import com.jec.base.dao.BaseDao;
import com.jec.module.sysconfig.entity.BroadcastConfig;
import org.springframework.stereotype.Repository;

/**
 * Created by jeremyliu on 05/10/2016.
 */
@Repository
public class BroadcastDao extends BaseDao<BroadcastConfig, Integer> implements GenericDAO<BroadcastConfig, Integer>{

    public boolean exist(int netunit, String code){
        Search search = new Search(BroadcastConfig.class);
        search.addFilterEqual("netunit", netunit);
        search.addFilterEqual("code", code);
        return searchUnique(search);
    }

    public boolean exist(String broadcaster){
        Search search = new Search(BroadcastConfig.class);
        search.addFilterEqual("broadcaster", broadcaster);
        return searchUnique(search);
    }
}
