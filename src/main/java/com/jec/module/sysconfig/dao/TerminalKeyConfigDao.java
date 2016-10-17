package com.jec.module.sysconfig.dao;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.googlecode.genericdao.search.Search;
import com.jec.base.dao.BaseDao;
import com.jec.module.sysconfig.entity.TerminalKeyConfig;
import org.springframework.stereotype.Repository;

/**
 * Created by jeremyliu on 09/10/2016.
 */
@Repository
public class TerminalKeyConfigDao extends BaseDao<TerminalKeyConfig, Integer> implements GenericDAO<TerminalKeyConfig, Integer>{

    public boolean exist(TerminalKeyConfig config){
        Search search = new Search(TerminalKeyConfig.class);
        search.addFilterNotEqual("id", config.getId());
        search.addFilterEqual("keyValue", config.getKeyValue());
        search.addFilterEqual("deviceNumber", config.getDeviceNumber());
        return searchUnique(search)!=null;
    }
}
