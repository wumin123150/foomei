package com.foomei.core.web.controller;

import com.foomei.common.base.annotation.LogIgnore;
import com.foomei.core.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Api(description = "日志管理")
@Controller
@RequestMapping(value = "/admin/log")
public class LogController {

  private static final String MENU = "log";

  @Autowired
  private LogService logService;

  @ApiOperation(value = "日志列表页面", httpMethod = "GET")
  @LogIgnore
  @RequiresRoles("admin")
  @RequestMapping
  public String list(Model model) {
    model.addAttribute("menu", MENU);
    return "admin/log/logList";
  }

}
