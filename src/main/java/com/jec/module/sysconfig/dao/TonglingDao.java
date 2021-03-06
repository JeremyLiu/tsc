package com.jec.module.sysconfig.dao;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.googlecode.genericdao.search.Search;
import com.jec.base.dao.BaseDao;
import com.jec.module.business.entity.Tongling;
import com.jec.module.sysconfig.entity.TonglingConfig;
import org.springframework.stereotype.Repository;


/**
 * Created by jeremyliu on 8/6/16.
 */
@Repository
public class TonglingDao extends BaseDao<TonglingConfig, Integer> implements GenericDAO<TonglingConfig, Integer>{

    public boolean exist(TonglingConfig tonglingConfig){
        Search search = new Search(TonglingConfig.class);
        search.addFilterEqual("id", tonglingConfig.getId());
        search.addFilterEqual("netunit", tonglingConfig.getNetunit());
        search.addFilterEqual("code", tonglingConfig.getCode());
        return searchUnique(search)!=null;
    }
}
