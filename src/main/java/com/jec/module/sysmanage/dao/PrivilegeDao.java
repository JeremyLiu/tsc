package com.jec.module.sysmanage.dao;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.jec.base.dao.BaseDao;
import com.jec.module.sysmanage.entity.Privilege;
import org.springframework.stereotype.Repository;

/**
 * Created by jeremyliu on 6/17/16.
 */
@Repository
public class PrivilegeDao extends BaseDao<Privilege, Integer> implements GenericDAO<Privilege, Integer>{
}
