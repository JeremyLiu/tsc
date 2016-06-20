package com.jec.base.dao;

import java.io.Serializable;
import java.util.*;
import java.util.Map.Entry;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;

public class BaseDao<T, ID extends Serializable> extends GenericDAOImpl<T, ID> {

    @Autowired
    @Override
    public void setSessionFactory(SessionFactory sessionFactory) {
        super.setSessionFactory(sessionFactory);
    }
    
    @SuppressWarnings("rawtypes")
	protected List execHQLQuery(String hql,List<Object> params)  
    {  
    	Query query=this.getSession().createQuery(hql);
    	
    	if(params!=null){
    		for(int i=0;i<params.size();i++){
    			if(params.get(i)!=null)
    				query.setParameter(i, params.get(i));
    		}
    	}
    	return query.list();
    }
    
    @SuppressWarnings("rawtypes")
	protected List execSQLQuery(String sql, List<Object> params){
    	Query query=this.getSession().createSQLQuery(sql);
    	
    	if(params!=null){
    		for(int i=0;i<params.size();i++){
    			if(params.get(i)!=null)
    				query.setParameter(i, params.get(i));
    		}
    	}
    	return query.list();
    }
    
    protected int updateSQLQuery(String sql,Map<String,Object> params){
    	Query query=this.getSession().createSQLQuery(sql);
    	
    	if(params!=null && params.size()>0){
    		for(Entry<String,Object>entry : params.entrySet()){
    			Object param=entry.getValue();
    			if(param!=null){
    	              if(param instanceof Collection<?>){  
    	                    query.setParameterList(entry.getKey(), (Collection<?>)param);  
    	                }else if(param instanceof Object[]){  
    	                    query.setParameterList(entry.getKey(), (Object[])param);  
    	                }else{  
    	                    query.setParameter(entry.getKey(), param);
    	                }
    			}
    		}
    	}
    	return query.executeUpdate();
    }

	protected String buildUpdate(Set<String> keys){
		String result = "", temp;
		if(keys == null)
			return "";
		Iterator<String> it = keys.iterator();

		if(it.hasNext()){
			temp = it.next();
			result += temp + " = :" + temp;
		}

		while(it.hasNext()){
			temp = it.next();
			result += ", " + temp + " = :" + temp;
		}

		return result;
	}
}

