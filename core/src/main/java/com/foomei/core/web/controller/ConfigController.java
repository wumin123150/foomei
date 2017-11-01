package com.foomei.core.web.controller;

import com.foomei.core.entity.Config;
import com.foomei.core.service.ConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Api(description = "系统配置管理")
@Controller
@RequestMapping(value = "/admin/config")
public class ConfigController {

  private static final String MENU = "config";
  private static final String ACTION_CREATE = "create";
  private static final String ACTION_UPDATE = "update";

  @Autowired
  private ConfigService configService;

  @ApiOperation(value = "系统配置列表页面", httpMethod = "GET")
  @RequiresRoles("admin")
  @RequestMapping
  public String list(Model model) {
    model.addAttribute("menu", MENU);
    return "admin/config/configList";
  }

  @ApiOperation(value = "系统配置新增页面", httpMethod = "GET")
  @RequiresRoles("admin")
  @RequestMapping(value = "create", method = RequestMethod.GET)
  public String createForm(Model model) {
    model.addAttribute("menu", MENU);
    model.addAttribute("action", ACTION_CREATE);

    model.addAttribute("config", new Config());
    return "admin/config/configForm";
  }

  @ApiOperation(value = "系统配置修改页面", httpMethod = "GET")
  @RequiresRoles("admin")
  @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
  public String updateForm(@PathVariable("id") Long id, Model model) {
    model.addAttribute("menu", MENU);
    model.addAttribute("action", ACTION_UPDATE);

    model.addAttribute("config", configService.get(id));
    return "admin/config/configForm";
  }

  @ApiOperation(value = "系统配置查看页面", httpMethod = "GET")
  @RequiresRoles("admin")
  @RequestMapping(value = "view", method = RequestMethod.GET)
  public String viewForm(Model model) {
    model.addAttribute("menu", "configs");

    model.addAttribute("configs", configService.getAll());
    return "admin/config/configView";
  }

}
