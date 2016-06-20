package com.jec.module.sysmonitor.dao;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.jec.base.dao.BaseDao;
import com.jec.module.sysmonitor.entity.CardType;
import org.springframework.stereotype.Repository;

/**
 * Created by jeremyliu on 5/9/16.
 */
@Repository
public class CardTypeDao extends BaseDao<CardType,Integer> implements GenericDAO<CardType,Integer>  {

}
