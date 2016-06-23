package com.jec.module.business.dao;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.jec.base.dao.BaseDao;
import com.jec.module.business.entity.Record;
import org.springframework.stereotype.Repository;

/**
 * Created by jeremyliu on 6/20/16.
 */
@Repository
public class RecordDao extends BaseDao<Record,Integer> implements GenericDAO<Record,Integer>{
}
