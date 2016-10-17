package com.jec.module.sysmanage.dao;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.jec.base.dao.BaseDao;
import com.jec.module.sysmanage.entity.OperateLog;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;

/**
 * Created by jeremyliu on 7/24/16.
 */
@Repository
public class SysLogDao extends BaseDao<OperateLog, Integer> implements GenericDAO<OperateLog, Integer>{

    @Resource
    private DataSourceTransactionManager txManager;

    public void saveForce(OperateLog[] operateLogs){
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 事物隔离级别，开启新事务，这样会比较安全些。
        TransactionStatus status = txManager.getTransaction(def); // 获得事务状态
        try {
            save(operateLogs);
            txManager.commit(status);
        } catch (Exception e) {
            txManager.rollback(status);
        }
    }
}
