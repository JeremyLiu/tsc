package com.jec.module.sysconfig.dao;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.googlecode.genericdao.search.Search;
import com.jec.base.dao.BaseDao;
import com.jec.module.sysconfig.entity.PhoneStation;
import org.springframework.stereotype.Repository;

/**
 * Created by jeremyliu on 08/10/2016.
 */
@Repository
public class PhoneStationDao extends BaseDao<PhoneStation, Integer> implements GenericDAO<PhoneStation, Integer>{

    public boolean exist(PhoneStation config){
        Search search = new Search(PhoneStation.class);
        search.addFilterNotEqual("id", config.getId());
        search.addFilterEqual("netunit", config.getNetunit());
        search.addFilterEqual("code", config.getCode());
        return searchUnique(search)!=null;
    }
}
