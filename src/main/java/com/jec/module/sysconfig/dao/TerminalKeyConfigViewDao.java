package com.jec.module.sysconfig.dao;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.googlecode.genericdao.search.Search;
import com.jec.base.dao.BaseDao;
import com.jec.module.sysconfig.entity.vo.TerminalKeyConfigView;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jeremyliu on 09/10/2016.
 */
@Repository
public class TerminalKeyConfigViewDao extends BaseDao<TerminalKeyConfigView, Integer> implements GenericDAO<TerminalKeyConfigView, Integer> {

    public List<TerminalKeyConfigView> findByNetUnit(int netUnitId){
        Search search = new Search(TerminalKeyConfigView.class);
        search.addFilterEqual("netunit", netUnitId);
        return search(search);
    }

    public List<TerminalKeyConfigView> findByDevice(String number){
        Search search = new Search(TerminalKeyConfigView.class);
        search.addFilterEqual("deviceNumber", number);
        return search(search);
    }
}
