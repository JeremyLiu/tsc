package com.jec.module.sysmanage.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jec.base.annotation.SysLog;
import com.jec.module.sysmanage.entity.Role;
import com.jec.module.sysmanage.service.SysLogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.jec.module.sysmanage.entity.User;
import com.jec.module.sysmanage.service.UserService;
import com.jec.utils.Response;

import java.net.URLDecoder;

@Controller
@RequestMapping(value = "/user")
public class UserController {
	
	@Resource
	private UserService userService;

	@Resource
	private SysLogService sysLogService;
	
	@RequestMapping(method = RequestMethod.POST, value = "login")
	public @ResponseBody
	Response login(HttpServletRequest request,
				   @RequestParam(value = "username") String username,
				   @RequestParam(value = "password") String password,
				   HttpSession httpSession){
		User user=userService.login(username, password);
		Response resp=Response.Builder(user);
		if(user==null)
			resp.message("用户名或密码错误！").status(1);
		else {
			httpSession.setAttribute("userId", user.getId());
			sysLogService.addLog(user.getId(), "0", "用户"+username+"登录系统");
		}
		return resp;
	}

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody
	Response getAllUser(){
		return Response.Builder(userService.getAll());
	}
	
	//注册
	@RequestMapping(method = RequestMethod.POST, value = "register")
	public @ResponseBody
	Response register(
			@RequestParam(value = "username") String username,
			@RequestParam(value="password") String password,
			@RequestParam(value = "role", required = false) Integer role){
		Response res = Response.Builder().status(Response.STATUS_PARAM_ERROR);
		if(role == null)
			role = Role.ROLE_USER;
		User user = userService.saveNewUser(username, password,role);
		if(user == null)
			return res.message("用户名已存在");
		sysLogService.addLog("添加用户"+username);
		return res.status(Response.STATUS_SUCCESS).data(user);
	}

	//修改密码
	@RequestMapping(method = RequestMethod.POST, value = "password/modify")
	@SysLog(action = "4-2", description = "修改密码")
	public @ResponseBody
	Response modifyPassword(
			@RequestParam(value = "newPassword") String newPassword,
			@RequestParam(value="oldPassword") String oldPassword,
			HttpSession httpSession){
		Response res = Response.Builder().status(Response.STATUS_PARAM_ERROR);
		int userId = (int)httpSession.getAttribute("userId");
		User user = userService.getUser(userId);
		if(!user.getPassword().equals(oldPassword)){
			res.message("旧密码输入错误!");
			return res;
		}

		if(newPassword.length() <= 0 || newPassword.length()>30){
			res.message("新密码不合法");
			return res;
		}

		user.setPassword(newPassword);
		userService.saveUser(user);
		sysLogService.addLog("修改密码");
		return res.status(Response.STATUS_SUCCESS);
	}

	@RequestMapping(method = RequestMethod.POST, value = "modify")
	public @ResponseBody
	Response modifyUser(@RequestParam(value = "name", required = false) String name,
						@RequestParam(value = "role", required = false) Integer role,
						@RequestParam(value = "password", required = false) String password,
						@RequestParam(value = "userId") int userId){
		Response res = Response.Builder().status(Response.STATUS_PARAM_ERROR);
		if(name != null && (name.length() == 0 || name.length() > 60))
			return res.message("用户名不合法");

		User user = userService.getUser(userId);
		String oldName = user.getName();
		if(user == null)
			return res.message("用户不存在");

		if(!oldName.equals(name) && userService.exist(name))
			return res.message("该用户名已被使用");

		if(password != null && (password.length() <= 0 || password.length()>30))
			return res.message("密码不能为空,且不能超过30个字符");

		boolean result=userService.modifyUser(userId,name, role, password);
		if(result) {
			String desc="修改用户"+oldName;
			if(name!=null && !name.equals(oldName))
				desc+= ",并更改新用户名为"+name;
			sysLogService.addLog(desc);
		}
		return res.status(Response.STATUS_SUCCESS).data(result);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "remove")
	public @ResponseBody
	Response removeUser(@RequestParam(value = "id") int id){
		if(id == 1)
			return Response.Builder().status(Response.STATUS_PARAM_ERROR).message("无法删除该用户");
		return Response.Builder(userService.removeUser(id));
	}

	@RequestMapping(method = RequestMethod.GET, value = "roles")
	public @ResponseBody
	Response allRoles(){
		return Response.Builder(userService.getAllRoles());
	}

	@RequestMapping(method = RequestMethod.POST, value = "role/create")
	public @ResponseBody
	Response addRole(@RequestParam(value = "name") String name,
						@RequestParam(value = "privilege", required = false, defaultValue = "") String privilege){
		Response res = Response.Builder().status(Response.STATUS_PARAM_ERROR);
		if(name.length() == 0 || name.length()>60)
			return res.message("角色名不合法");
		return res.status(Response.STATUS_SUCCESS).data(userService.addUserRole(name,privilege.split(",")));
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "role/remove")
	public @ResponseBody
	Response removeRole(@RequestParam(value = "id") int id){
		Response res = Response.Builder();
		boolean result = userService.removeRole(id);
		if(!result)
			return res.status(Response.STATUS_PARAM_ERROR).message("该角色已分配给用户,无法删除");
		else
			return res.data(true);
	}

	@RequestMapping(method = RequestMethod.POST, value = "role/modify", consumes = "application/json;charset=UTF-8")
	public @ResponseBody
	Response modifyRole(@RequestBody(required = false) String[] privilege,
						@RequestParam(value = "id") int id,
						@RequestParam(value = "name", required = false) String name){
		boolean result = true;
		Response res = Response.Builder();
		if(privilege != null)
			result = userService.modifyRolePrivilege(id, privilege) && result;
		if(name != null){
			try {
				name = new String(name.getBytes("ISO-8859-1"), "UTF-8");
			}catch(Exception e){
				e.printStackTrace();
			}
			if(name.length() == 0 || name.length() > 60)
				return res.status(Response.STATUS_PARAM_ERROR).message("名字不合法");
			else
				result = result && userService.modifyRoleName(id, name);
		}

		return res.data(result);
	}
}
