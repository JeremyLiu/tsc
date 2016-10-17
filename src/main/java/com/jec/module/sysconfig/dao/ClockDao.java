package com.jec.module.sysconfig.dao;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.jec.base.dao.BaseDao;
import com.jec.module.sysconfig.entity.Clock;
import org.springframework.stereotype.Repository;

/**
 * Created by jeremyliu on 8/6/16.
 */
@Repository
public class ClockDao extends BaseDao<Clock,Integer> implements GenericDAO<Clock, Integer> {

}
