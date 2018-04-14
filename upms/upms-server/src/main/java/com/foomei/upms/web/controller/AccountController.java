package com.foomei.upms.web.controller;

import com.foomei.upms.service.AnnexService;
import com.foomei.upms.service.MessageService;
import com.foomei.upms.service.UserService;
import com.foomei.upms.web.CoreThreadContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Api(description = "账户管理")
@Controller
public class AccountController {

  @Autowired
  private UserService userService;
  @Autowired
  private AnnexService annexService;
  @Autowired
  private MessageService messageService;

  @Value("${system.theme:ace}")
  private String theme;

  @ApiOperation(value = "修改账户页面", httpMethod = "GET")
  @RequestMapping(value = "/{action}/changeAccount", method = RequestMethod.GET)
  public String changeAccountForm(@PathVariable("action") String action, Model model) {
    model.addAttribute("action", action);
    model.addAttribute("user", userService.get(CoreThreadContext.getUserId()));
    return theme + "/account/changeAccount";
  }

  @ApiOperation(value = "我的消息页面", httpMethod = "GET")
  @RequestMapping(value = "/{action}/readMessage", method = RequestMethod.GET)
  public String messageForm(@PathVariable("action") String action, Model model) {
    model.addAttribute("action", action);
    return theme + "/account/readMessage";
  }

  @ApiOperation(value = "我的消息页面", httpMethod = "GET")
  @RequestMapping(value = "/{action}/message/read/{id}")
  public String message(@PathVariable("action") String action, @PathVariable("id") String id, Model model) {
    messageService.read(id);
    return "redirect:/"+action+"/readMessage";
  }

}
