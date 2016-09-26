package com.jec.module.sysconfig.dao;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.googlecode.genericdao.search.Search;
import com.jec.base.dao.BaseDao;
import com.jec.module.sysconfig.entity.Clock;
import com.jec.module.sysconfig.entity.DigitTrunk;
import org.springframework.stereotype.Repository;

/**
 * Created by jeremyliu on 9/25/16.
 */
@Repository
public class DigitTrunkDao extends BaseDao<DigitTrunk,Integer> implements GenericDAO<DigitTrunk, Integer> {

    public boolean exist(int netunit,int slot, int port){
        Search search = new Search(DigitTrunk.class);
        search.addFilterEqual("netunit", netunit);
        search.addFilterEqual("slot", slot);
        search.addFilterEqual("port", port);
        return searchUnique(search)!=null;
    }
}
