package com.foomei.core.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foomei.core.service.RoleService;
import com.foomei.core.service.UserGroupService;

@Api(description = "机构管理") 
@Controller
@RequestMapping(value = "/admin/userGroup")
public class UserGroupController {

	private static final String MENU = "userGroup";

	@Autowired
	private UserGroupService userGroupService;
	@Autowired
	private RoleService roleService;

	@ApiOperation(value = "机构列表页面", httpMethod = "GET")
	@RequiresRoles("admin")
	@RequestMapping
	public String list(Model model) {
		model.addAttribute("menu", MENU);
		model.addAttribute("roles", roleService.getAll());
		return "admin/userGroup/userGroupList";
	}

}
