package com.jec.module.sysconfig.dao;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.googlecode.genericdao.search.Search;
import com.jec.base.dao.BaseDao;
import com.jec.module.sysconfig.entity.MeetingConfig;
import org.springframework.stereotype.Repository;

/**
 * Created by jeremyliu on 8/6/16.
 */
@Repository
public class MeetingDao extends BaseDao<MeetingConfig, Integer> implements GenericDAO<MeetingConfig, Integer>{

    public boolean exist(MeetingConfig config){
        Search search = new Search(MeetingConfig.class);
        search.addFilterNotEqual("id", config.getId());
        search.addFilterEqual("netunit", config.getNetunit());
        search.addFilterEqual("code", config.getCode());
        return searchUnique(search)!=null;
    }
}
