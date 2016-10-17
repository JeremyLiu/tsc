package com.jec.module.sysmanage.dao;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.jec.base.dao.BaseDao;
import com.jec.module.sysmanage.entity.Role;
import org.springframework.stereotype.Repository;

/**
 * Created by jeremyliu on 6/13/16.
 */
@Repository
public class RoleDao extends BaseDao<Role,Integer> implements GenericDAO<Role,Integer> {

}
