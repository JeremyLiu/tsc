package com.jec.module.sysmonitor.dao;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.googlecode.genericdao.search.Search;
import com.jec.base.dao.BaseDao;
import com.jec.module.sysmonitor.entity.NetUnit;
import com.jec.module.sysmonitor.entity.TerminalDevice;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jeremyliu on 5/10/16.
 */
@Repository
public class TerminalDeviceDao extends BaseDao<TerminalDevice,Integer> implements GenericDAO<TerminalDevice,Integer> {

    public int removeDeviceByNetUnit(int netUnit){
        String sql = "delete from zhwg_terminal_device where " +
                "card_id in (select id from zhwg_device_card where element_id = :netUnit)";

        Map<String,Object> params = new HashMap<String,Object>();
        params.put("netUnit", netUnit);
        return this.updateSQLQuery(sql, params);
    }

    public int removeDeviceByCard(Integer[] cards){
        if(cards.length == 0)
            return 0;
        String sql = "delete from zhwg_terminal_device where card_id in :cards";
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("cards", cards);
        return this.updateSQLQuery(sql, params);
    }

    public List<TerminalDevice> findByNetunit(int netunit){
        Search search = new Search(TerminalDevice.class);
        search.addFilterEqual("netUnitId", netunit);
        return search(search);
    }

    public TerminalDevice findByCode(String code){
        Search search = new Search(TerminalDevice.class);
        search.addFilterEqual("code", code);
        return searchUnique(search);
    }
}
