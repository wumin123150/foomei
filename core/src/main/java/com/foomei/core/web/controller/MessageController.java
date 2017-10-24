package com.foomei.core.web.controller;

import com.foomei.core.entity.Message;
import com.foomei.core.entity.MessageText;
import com.foomei.core.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Api(description = "消息管理")
@Controller
@RequestMapping(value = "/admin/message")
public class MessageController {

  private static final String MENU = "message";
  private static final String ACTION_CREATE = "create";
  private static final String ACTION_UPDATE = "update";

  @Autowired
  private MessageService messageService;

  @ApiOperation(value = "消息列表页面", httpMethod = "GET")
  @RequiresRoles("admin")
  @RequestMapping
  public String list(Model model) {
    model.addAttribute("menu", MENU);
    return "admin/message/messageList";
  }

  @ApiOperation(value = "消息新增页面", httpMethod = "GET")
  @RequiresRoles("admin")
  @RequestMapping(value = "create", method = RequestMethod.GET)
  public String createForm(Model model) {
    model.addAttribute("menu", MENU);
    model.addAttribute("action", ACTION_CREATE);

    model.addAttribute("text", new MessageText());
    return "admin/message/messageForm";
  }

  @ApiOperation(value = "消息新增", httpMethod = "POST")
  @RequiresRoles("admin")
  @RequestMapping(value = "create", method = RequestMethod.POST)
  public String create(@Valid MessageText text, BindingResult result, @RequestParam(value = "users") List<Long> checkedUsers, Model model, RedirectAttributes redirectAttributes) {
    if (result.hasErrors()) {
      model.addAttribute("menu", MENU);
      model.addAttribute("action", ACTION_CREATE);

      model.addAttribute("text", text);

      model.addAttribute("errors", result);
      return "admin/message/messageForm";
    } else {
      List<Message> messages = messageService.save(text.getContent(), null, checkedUsers);
    }

    redirectAttributes.addFlashAttribute("message", "新增消息成功");
    return "redirect:/admin/message";
  }

}
