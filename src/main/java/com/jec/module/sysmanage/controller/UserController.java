package com.jec.module.sysmanage.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.jec.module.sysmanage.entity.Role;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jec.module.sysmanage.entity.User;
import com.jec.module.sysmanage.service.UserService;
import com.jec.utils.Response;

@Controller
@RequestMapping(value = "/user")
public class UserController {
	
	@Resource
	private UserService userService;
	
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
		else
			httpSession.setAttribute("userId",user.getId());
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
		return res.status(Response.STATUS_SUCCESS).data(user);
	}

	//修改密码
	@RequestMapping(method = RequestMethod.POST, value = "password/modify")
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

		return res;
	}

	@RequestMapping(method = RequestMethod.POST, value = "modify")
	public @ResponseBody
	Response modifyUser(@RequestParam(value = "name", required = false) String name,
						@RequestParam(value = "role", required = false) Integer role,
						@RequestParam(value = "userId") int userId){
		Response res = Response.Builder().status(Response.STATUS_PARAM_ERROR);
		if(name.length() == 0 || name.length() > 60)
			return res.message("用户名不合法");

		if(userService.exist(name))
			return res.message("该用户名已被使用");

		return res.status(Response.STATUS_SUCCESS).data(userService.modifyUser(userId,name, role));
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
}
