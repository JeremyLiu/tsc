package com.jec.module.sysconfig.dao;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.googlecode.genericdao.search.Search;
import com.jec.base.dao.BaseDao;
import com.jec.module.sysconfig.entity.TerminalBusiness;
import org.springframework.stereotype.Repository;

/**
 * Created by jeremyliu on 09/10/2016.
 */
@Repository
public class TerminalBusinessDao extends BaseDao<TerminalBusiness, Integer> implements GenericDAO<TerminalBusiness, Integer>{

    public boolean exist(TerminalBusiness terminalBusiness){
        Search search = new Search(TerminalBusiness.class);
        search.addFilterNotEqual("id", terminalBusiness.getId());
        search.addFilterEqual("keyConfigId", terminalBusiness.getKeyConfigId());
        search.addFilterEqual("deviceNumber", terminalBusiness.getDeviceNumber());
        return searchUnique(search) != null;
    }
}
