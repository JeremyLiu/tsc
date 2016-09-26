package com.jec.module.sysmonitor.dao;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.googlecode.genericdao.search.Search;
import com.jec.base.dao.BaseDao;
import com.jec.module.sysmonitor.entity.Card;
import com.jec.protocol.pdu.PduConstants;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jeremyliu on 4/11/16.
 */
@Repository
public class CardDao extends BaseDao<Card,Integer> implements GenericDAO<Card,Integer> {

    public int removeByNetUnit(int netUnit){
        String sql = "delete from zhwg_device_card where element_id = :netUnit";
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("netUnit", netUnit);
        return this.updateSQLQuery(sql, params);
    }

    public int updateNetUnit(int oldNetunit,int newNetUnit) {
        String sql = "update zhwg_device_card set element_id = :newNetUnit where element_id = :oldNetunit";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("oldNetunit", oldNetunit);
        params.put("newNetUnit", newNetUnit);
        return this.updateSQLQuery(sql, params);
    }

    public List<Card> getCardByNetunit(int netUnit){
        Search search = new Search(Card.class);
        search.addFilterEqual("netUnitId", netUnit);
        return search(search);
    }

    public Card getMainCard(int netUnit){
        Search search = new Search(Card.class);
        search.addFilterEqual("netUnitId", netUnit);
        search.addFilterEqual("type", PduConstants.CARD_TYPE_MCB);
        return searchUnique(search);
    }

    public Card getCardBySlot(int netunit, int slot){
        Search search = new Search(Card.class);
        search.addFilterEqual("netUnitId", netunit);
        search.addFilterEqual("slotNumber", slot);
        return searchUnique(search);
    }

}
