package com.foomei.core.web.controller;

import com.foomei.core.web.CoreThreadContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.foomei.common.base.annotation.LogIgnore;
import com.foomei.core.entity.User;
import com.foomei.core.service.UserService;

@Api(description = "账户管理")  
@Controller
public class AccountController {
	
	@Autowired
	private UserService userService;
	
	@ApiOperation(value = "修改密码页面", httpMethod = "GET")
	@RequestMapping(value = "/{action}/changePwd", method = RequestMethod.GET)
	public String changePasswordForm(@PathVariable("action") String action, Model model) {
		model.addAttribute("action", action);
		model.addAttribute("user", userService.get(CoreThreadContext.getUserId()));
		return "user/changePassword";
	}
	
	@ApiOperation(value = "密码修改", httpMethod = "POST")
	@LogIgnore(value = "field", excludes = {"plainPassword"})
	@RequestMapping(value = "/{action}/changePwd", method = RequestMethod.POST)
	public String changePassword(@PathVariable("action") String action, String plainPassword, Model model, RedirectAttributes redirectAttributes) {
		ObjectError error = null;
		if(StringUtils.isEmpty(plainPassword)) {
			error = new ObjectError("password", "密码不能为空");
		} 
		
		if(error != null) {
			model.addAttribute("user", userService.get(CoreThreadContext.getUserId()));
			
			model.addAttribute("error", error);
			return "user/changePassword";
		} else 
			userService.changePassword(CoreThreadContext.getUserId(), plainPassword);

		redirectAttributes.addFlashAttribute("message", "修改密码成功");
		return "redirect:/"+action+"/index";
	}
	
	@ApiOperation(value = "修改账户页面", httpMethod = "GET")
	@RequestMapping(value = "/{action}/changeAccount", method = RequestMethod.GET)
	public String changeAccountForm(@PathVariable("action") String action, Model model) {
		model.addAttribute("action", action);
		model.addAttribute("user", userService.get(CoreThreadContext.getUserId()));
		return "user/changeAccount";
	}
	
	@ApiOperation(value = "账户修改", httpMethod = "POST")
	@RequestMapping(value = "/{action}/changeAccount", method = RequestMethod.POST)
	public String changeAccount(@PathVariable("action") String action, String name, String phone, String email, Model model, RedirectAttributes redirectAttributes) {
		ObjectError error = null;
		if(StringUtils.isEmpty(name)) {
			error = new ObjectError("name", "姓名不能为空");
		} 
		
		if(error != null) {
			model.addAttribute("user", userService.get(CoreThreadContext.getUserId()));
			
			model.addAttribute("error", error);
			return "user/changeAccount";
		} 
		
		User currentUser = userService.get(CoreThreadContext.getUserId());
		currentUser.setName(name);
		currentUser.setMobile(phone);
		currentUser.setEmail(email);
		userService.save(currentUser);

		redirectAttributes.addFlashAttribute("message", "修改账户成功");
		return "redirect:/"+action+"/index";
	}

}
