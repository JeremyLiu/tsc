package com.jec.module.business.dao;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.jec.base.dao.BaseDao;
import com.jec.module.business.entity.Record;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;

/**
 * Created by jeremyliu on 6/20/16.
 */
@Repository
public class RecordDao extends BaseDao<Record,Integer> implements GenericDAO<Record,Integer>{

    @Resource
    private DataSourceTransactionManager txManager;

    public void saveForce(Record record){
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW); // 事物隔离级别，开启新事务，这样会比较安全些。
        TransactionStatus status = txManager.getTransaction(def); // 获得事务状态
        try {
            save(record);
            txManager.commit(status);
        } catch (Exception e) {
            txManager.rollback(status);
        }
    }
}
