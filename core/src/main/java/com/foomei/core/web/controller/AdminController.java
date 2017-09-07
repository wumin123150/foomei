package com.foomei.core.web.controller;

import io.swagger.annotations.Api;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 管理员首页
 *
 * @author walker
 */
@Api(description = "管理员引导")
@Controller
public class AdminController {

  @RequiresRoles("admin")
  @RequestMapping(value = "/admin/index", method = RequestMethod.GET)
  public String index() {
    return "admin/index";
  }

}
