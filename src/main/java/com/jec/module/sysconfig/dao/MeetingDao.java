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

    public boolean exist(int netunit, String code){
        Search search = new Search(MeetingConfig.class);
        search.addFilterEqual("netunit", netunit);
        search.addFilterEqual("code", code);
        return searchUnique(search)!=null;
    }
}
