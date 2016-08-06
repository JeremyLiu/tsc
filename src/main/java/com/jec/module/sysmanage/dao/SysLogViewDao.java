package com.jec.module.sysmanage.dao;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.jec.base.dao.BaseDao;
import com.jec.module.sysmanage.entity.view.OperateLogView;
import org.springframework.stereotype.Repository;

/**
 * Created by jeremyliu on 7/24/16.
 */
@Repository
public class SysLogViewDao extends BaseDao<OperateLogView, Integer> implements GenericDAO<OperateLogView, Integer> {
}
