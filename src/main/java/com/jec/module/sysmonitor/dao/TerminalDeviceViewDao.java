package com.jec.module.sysmonitor.dao;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.jec.base.dao.BaseDao;
import com.jec.module.sysmonitor.entity.view.TerminalDeviceView;
import org.springframework.stereotype.Repository;

/**
 * Created by jeremyliu on 5/10/16.
 */
@Repository
public class TerminalDeviceViewDao extends BaseDao<TerminalDeviceView,Integer> implements GenericDAO<TerminalDeviceView,Integer> {

}
