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

    public boolean exist(DigitTrunk digitTrunk){
        Search search = new Search(DigitTrunk.class);
        search.addFilterNotEqual("id", digitTrunk.getId());
        search.addFilterEqual("netunit", digitTrunk.getNetunit());
        search.addFilterEqual("slot", digitTrunk.getSlot());
        search.addFilterEqual("port", digitTrunk.getPort());
        return searchUnique(search)!=null;
    }
}
