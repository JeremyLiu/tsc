package com.jec.module.sysmonitor.dao;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.googlecode.genericdao.search.Search;
import com.jec.base.dao.BaseDao;
import com.jec.module.sysmonitor.entity.Card;
import com.jec.module.sysmonitor.entity.view.CardView;
import org.springframework.stereotype.Repository;

/**
 * Created by jeremyliu on 5/10/16.
 */
@Repository
public class CardViewDao extends BaseDao<CardView,Integer> implements GenericDAO<CardView,Integer> {

    public CardView getCardBySlot(int netunit, int slot){
        Search search = new Search(CardView.class);
        search.addFilterEqual("netUnitId", netunit);
        search.addFilterEqual("slot", slot);
        return searchUnique(search);
    }
}
