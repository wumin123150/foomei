package com.foomei.core.web.controller;

import io.swagger.annotations.Api;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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

  @Value("${system.theme:ace}")
  private String theme;

  @RequiresRoles("admin")
  @RequestMapping(value = "/admin/index", method = RequestMethod.GET)
  public String index() {
    return theme + "/index";
  }

  @RequestMapping(value = "/admin/home", method = RequestMethod.GET)
  public String home() {
    return theme + "/home";
  }

  @RequestMapping(value = "/layui/user", method = RequestMethod.GET)
  public String user(Model model) {
    return "layui/user/userList";
  }

  @RequestMapping(value = "/layui/role", method = RequestMethod.GET)
  public String role(Model model) {
    return "layui/role/roleList";
  }

  @RequestMapping(value = "/layui/role/create", method = RequestMethod.GET)
  public String roleCreate(Model model) {
    return "layui/role/roleForm";
  }

  @RequestMapping(value = "/layui/role/update/{id}", method = RequestMethod.GET)
  public String roleUpdate(@PathVariable("id") Long id, Model model) {
    model.addAttribute("id", id);
    return "layui/role/roleForm";
  }

  @RequestMapping(value = "/layui/log", method = RequestMethod.GET)
  public String log(Model model) {
    return "layui/log/logList";
  }

  @RequestMapping(value = "/layui/log/view/{id}", method = RequestMethod.GET)
  public String log(@PathVariable("id") String id, Model model) {
    model.addAttribute("id", id);
    return "layui/log/logView";
  }

}
