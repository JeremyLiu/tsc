package com.jec.module.sysconfig.dao;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.googlecode.genericdao.search.Search;
import com.jec.base.dao.BaseDao;
import com.jec.module.sysconfig.entity.vo.TerminalBusinessView;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jeremyliu on 09/10/2016.
 */
@Repository
public class TerminalBusinessViewDao extends BaseDao<TerminalBusinessView, Integer> implements GenericDAO<TerminalBusinessView, Integer> {

    public List<TerminalBusinessView> findByNetUnit(int netUnitId){
        Search search = new Search(TerminalBusinessView.class);
        search.addFilterEqual("netunit", netUnitId);
        return search(search);
    }

    public List<TerminalBusinessView> findByDevice(String number){
        Search search = new Search(TerminalBusinessView.class);
        search.addFilterEqual("deviceNumber", number);
        return search(search);
    }
}