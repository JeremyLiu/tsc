package com.jec.module.sysmanage.dao;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.googlecode.genericdao.search.Search;
import com.jec.base.dao.BaseDao;
import com.jec.module.sysmanage.entity.UserResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by jeremyliu on 07/10/2016.
 */
@Repository
public class UserResourceDao extends BaseDao<UserResource, Integer> implements GenericDAO<UserResource, Integer>{

    @Transactional(readOnly = true)
    public List<UserResource> getLogReousrce(){
        Search search = new Search(UserResource.class);
        search.addFilterEqual("log", true);
        return search(search);
    }
}
