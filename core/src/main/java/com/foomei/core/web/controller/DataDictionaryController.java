package com.foomei.core.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.foomei.core.service.DataDictionaryService;
import com.foomei.core.service.DataTypeService;

@Api(description = "数据字典管理")
@Controller
@RequestMapping(value = "/admin/dataDictionary")
public class DataDictionaryController {

	private static final String MENU = "dataDictionary";

	@Autowired
	private DataTypeService dataTypeService;
	@Autowired
	private DataDictionaryService dataDictionaryService;

	@ApiOperation(value = "数据字典页面", httpMethod = "GET")
	@RequiresRoles("admin")
	@RequestMapping
	public String list(String typeCode, Model model) {
		model.addAttribute("menu", MENU);
		
		model.addAttribute("type", dataTypeService.getByCode(typeCode));

		return "admin/dataDictionary/dataDictionary";
	}
	
}
