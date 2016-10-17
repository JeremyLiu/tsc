package com.jec.module.sysconfig.dao;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.googlecode.genericdao.search.Search;
import com.jec.base.dao.BaseDao;
import com.jec.module.sysconfig.entity.NumberEntry;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by jeremyliu on 02/10/2016.
 */
@Repository
public class NumberEntryDao extends BaseDao<NumberEntry, Integer> implements GenericDAO<NumberEntry, Integer> {

    public boolean exist(NumberEntry numberEntry){
        Search search = new Search(NumberEntry.class);
        search.addFilterNotEqual("id", numberEntry.getId());
        search.addFilterEqual("netunit", numberEntry.getNetunit());
        search.addFilterEqual("slot", numberEntry.getSlot());
        search.addFilterEqual("ts", numberEntry.getTs());
        return searchUnique(search) != null;
    }

    public boolean exist(int id, String number){
        Search search = new Search(NumberEntry.class);
        search.addFilterNotEqual("id", id);
        search.addFilterEqual("number", number);
        return searchUnique(search) != null;
    }

    public List<NumberEntry> findNumbers(String[] numbers){
        Search search = new Search(NumberEntry.class);
        search.addFilterIn("number", Arrays.asList(numbers));
        return search(search);
    }

    @Transactional(readOnly = true)
    public List<Map> getAllNumbers(){
        String hql = "select new map(number as number, " +
                "userLevel as userLevel) " +
                "from NumberEntry";
        return execHQLQuery(hql, null);
    }
}
