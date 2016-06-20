package com.jec.module.sysmanage.service;

import java.util.List;

import javax.annotation.Resource;
import javax.jws.soap.SOAPBinding;

import com.jec.module.sysmanage.dao.RoleDao;
import com.jec.module.sysmanage.entity.Privilege;
import com.jec.module.sysmanage.entity.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.genericdao.search.Search;
import com.jec.module.sysmanage.dao.UserDao;
import com.jec.module.sysmanage.entity.User;
import org.springframework.web.bind.annotation.RequestMapping;


@Service
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class UserService {
	
	@Resource
	private UserDao userDao;

	@Resource
	private RoleDao roleDao;

	@Transactional(propagation = Propagation.REQUIRED)
	public List<User> getAll()
	{
		//return users;
		return userDao.findAll();
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public User login(String username, String password) {
		// TODO Auto-generated method stub
		Search search=new Search(User.class);
		
		search.addFilterEqual("name", username);
		search.addFilterEqual("password",password);
		
		List<User> users=userDao.search(search);
		
		
		if(users.size()==0)
			return null;
		else
			return users.get(0);
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public boolean exist(String name){

		Search search = new Search(User.class);
		search.addFilterEqual("name", name);

		return userDao.search(search).size()>0;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public User saveNewUser(String username, String password, int role){
		Search search = new Search(User.class);
		search.addFilterEqual("name", username);
		if(userDao.search(search).size()>0)
			return null;

		if(roleDao.find(role) == null)
			return null;

		User user = new User();
		user.setCreateTime();
		user.setName(username);
		user.setPassword(password);
		user.setRoleId(role);
		userDao.save(user);
		return user;
	}

	@Transactional(readOnly = true)
	public User getUser(int userId){
		return userDao.find(userId);
	}

	@Transactional
	public void saveUser(User user){
		userDao.save(user);
	}

	@Transactional
	public boolean modifyUser(int userId, String name, Integer role){
		return userDao.updateUser(userId, name, role) > 0;
	}

	@Transactional
	public boolean removeUser(int id){
		User user = userDao.find(id);
		if(user == null)
			return false;
		else
			userDao.remove(user);
		return true;
	}

	@Transactional(readOnly = true)
	public List<Role> getAllRoles(){
		return roleDao.findAll();
	}

	@Transactional
	public boolean removeRole(int role){
		Search search = new Search(User.class);
		search.addFilterEqual("roleId", role);
		if(userDao.search(search).size()>0)
			return false;
		roleDao.removeById(role);
		return true;
	}
	@Transactional(readOnly = true)
	public Role getUserRole(int userId){
		User user = userDao.find(userId);
		return roleDao.find(user.getRoleId());
	}
}
