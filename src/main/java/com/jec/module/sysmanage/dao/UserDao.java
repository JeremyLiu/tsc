package com.jec.module.sysmanage.dao;

import com.jec.base.dao.BaseDao;
import org.springframework.stereotype.Repository;

import com.googlecode.genericdao.dao.hibernate.GenericDAO;
import com.jec.module.sysmanage.entity.User;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserDao extends BaseDao<User,Integer> implements GenericDAO<User,Integer>{

    public int updateUser(int userId, String name, Integer role, String password){
        Map<String, Object> param = new HashMap<>();
        if(name != null)
            param.put("name", name);
        if(role != null)
            param.put("role_id", role);
        if(password != null)
            param.put("password", password);
        if(param.size() == 0)
            return 0;
        String sql = buildUpdate(param.keySet());
        param.put("id", userId);

        sql = "update zhwg_user set " + sql + " where id = :id";
        return updateSQLQuery(sql, param);
    }
}
