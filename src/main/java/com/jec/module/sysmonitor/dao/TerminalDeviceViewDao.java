package com.jec.module.sysmonitor.dao;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.googlecode.genericdao.search.Search;
import com.jec.base.dao.BaseDao;
import com.jec.module.sysmonitor.entity.view.TerminalDeviceView;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jeremyliu on 5/10/16.
 */
@Repository
public class TerminalDeviceViewDao extends BaseDao<TerminalDeviceView,Integer> implements GenericDAO<TerminalDeviceView,Integer> {
    public List<TerminalDeviceView> findByNetunit(int netunit){
        Search search = new Search(TerminalDeviceView.class);
        search.addFilterEqual("netUnitId", netunit);
        return search(search);
    }
}
