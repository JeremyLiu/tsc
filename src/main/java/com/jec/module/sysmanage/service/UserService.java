package com.jec.module.sysmanage.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.jec.module.sysmanage.dao.PrivilegeDao;
import com.jec.module.sysmanage.dao.RoleDao;
import com.jec.module.sysmanage.entity.Privilege;
import com.jec.module.sysmanage.entity.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.genericdao.search.Search;
import com.jec.module.sysmanage.dao.UserDao;
import com.jec.module.sysmanage.entity.User;


@Service
@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
public class UserService {
	
	@Resource
	private UserDao userDao;

	@Resource
	private RoleDao roleDao;

	@Resource
	private PrivilegeDao privilegeDao;

	@Resource
	private SysLogService sysLogService;

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

		sysLogService.addLog("4-0","创建新用户" + username);
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
	public boolean modifyUser(int userId, String name, Integer role, String password){
		if(userDao.updateUser(userId, name, role, password) > 0) {
			return true;
		}
		else
			return false;
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
		Search search = new Search(Role.class);
		search.addFilterGreaterThan("id", 0);
		return roleDao.search(search);
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

	@Transactional
	public Role addUserRole(String name, String[] privilege){
		Role role = new Role();
		role.setName(name);
		role.setCreateTime(new Date());
		List<Privilege> privileges = new ArrayList<>();

		roleDao.save(role);
		for(String pStr: privilege){
			if(pStr.length() > 0) {
				Privilege p = new Privilege();
				p.setValue(pStr);
				privileges.add(p);
				p.setRole(role.getId());
				privilegeDao.save(p);
			}
		}
		role.setPrivilege(privileges);
		sysLogService.addLog("添加角色" + name);
		return role;
	}

	@Transactional
	public boolean modifyRolePrivilege(int id, String[] privilege){
		Role role = roleDao.find(id);
		if(role == null)
			return false;
		Search search = new Search(Privilege.class);
		search.addFilterEqual("role", id);

		List<Privilege> origin = privilegeDao.search(search);

		int len1 = privilege.length;
		int len2 = origin.size();

		for(int i = 0; i< len2; i++){
			if(i<len1)
				origin.get(i).setValue(privilege[i]);
			else
				privilegeDao.remove(origin.get(i));
		}

		for(int i=len2; i<len1; i++){
			Privilege p = new Privilege();
			p.setValue(privilege[i]);
			p.setRole(id);
			privilegeDao.save(p);
		}

		sysLogService.addLog("修改角色" + role.getName() + "权限");
		return true;
	}

	@Transactional
	public boolean modifyRoleName(int id, String name){
		Role role = roleDao.find(id);
		if(role == null)
			return false;
		String oldName = role.getName();
		role.setName(name);
		roleDao.save(role);
		sysLogService.addLog("修改角色名" + oldName);
		return true;
	}
}
