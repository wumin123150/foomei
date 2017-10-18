package com.foomei.core.web.controller;

import com.foomei.common.base.annotation.LogIgnore;
import com.foomei.core.entity.Annex;
import com.foomei.core.entity.User;
import com.foomei.core.service.AnnexService;
import com.foomei.core.service.MessageService;
import com.foomei.core.service.UserService;
import com.foomei.core.web.CoreThreadContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Api(description = "账户管理")
@Controller
public class AccountController {

  @Autowired
  private UserService userService;
  @Autowired
  private AnnexService annexService;
  @Autowired
  private MessageService messageService;

  @ApiOperation(value = "修改账户页面", httpMethod = "GET")
  @RequestMapping(value = "/{action}/changeAccount", method = RequestMethod.GET)
  public String changeAccountForm(@PathVariable("action") String action, Model model) {
    model.addAttribute("action", action);
    model.addAttribute("user", userService.get(CoreThreadContext.getUserId()));
    return "user/changeAccount";
  }

  @ApiOperation(value = "账户修改", httpMethod = "POST")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "name", value = "姓名", required = true, dataType = "string", paramType = "form"),
    @ApiImplicitParam(name = "phone", value = "手机", dataType = "string", paramType = "form"),
    @ApiImplicitParam(name = "email", value = "邮箱", dataType = "string", paramType = "form")
  })
  @RequestMapping(value = "/{action}/changeAccount", method = RequestMethod.POST)
  public String changeAccount(@PathVariable("action") String action, String name, String phone, String email, Model model, RedirectAttributes redirectAttributes) {
    ObjectError error = null;
    if (StringUtils.isEmpty(name)) {
      error = new ObjectError("name", "姓名不能为空");
    }

    if (error != null) {
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
    return "redirect:/" + action + "/index";
  }

  @ApiOperation(value = "头像修改", httpMethod = "POST")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "file", value = "头像", required = true, dataType = "file", paramType = "form")
  })
  @RequestMapping(value = "/{action}/changeAvatar", method = RequestMethod.POST)
  public String changeAvatar(@PathVariable("action") String action, @RequestParam(value = "file") MultipartFile file, Model model, RedirectAttributes redirectAttributes) {
    User user = userService.get(CoreThreadContext.getUserId());

    try {
      Annex annex = annexService.save(file.getBytes(), file.getOriginalFilename(), User.USER_ANNEX_PATH, String.valueOf(user.getId()), User.USER_ANNEX_TYPE);
      if (annex != null) {
        user.setAvatar(annex.getPath());
        userService.save(user);
      }
    } catch (Exception e) {

    }

    redirectAttributes.addFlashAttribute("message", "修改头像成功");
    return "redirect:/" + action + "/index";
  }

  @ApiOperation(value = "密码修改", httpMethod = "POST")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "plainPassword", value = "新密码", required = true, dataType = "string", paramType = "form")
  })
  @LogIgnore(value = "field", excludes = {"plainPassword"})
  @RequestMapping(value = "/{action}/changePwd", method = RequestMethod.POST)
  public String changePassword(@PathVariable("action") String action, String plainPassword, Model model, RedirectAttributes redirectAttributes) {
    ObjectError error = null;
    if (StringUtils.isEmpty(plainPassword)) {
      error = new ObjectError("password", "密码不能为空");
    }

    if (error != null) {
      model.addAttribute("user", userService.get(CoreThreadContext.getUserId()));

      model.addAttribute("error", error);
      return "user/changeAccount";
    } else
      userService.changePassword(CoreThreadContext.getUserId(), plainPassword);

    redirectAttributes.addFlashAttribute("message", "修改密码成功");
    return "redirect:/" + action + "/index";
  }

  @ApiOperation(value = "我的消息账户页面", httpMethod = "GET")
  @RequestMapping(value = "/{action}/readMessage", method = RequestMethod.GET)
  public String messageForm(@PathVariable("action") String action, Model model) {
    model.addAttribute("action", action);
    return "user/readMessage";
  }

  @ApiOperation(value = "我的消息账户页面", httpMethod = "GET")
  @RequestMapping(value = "/{action}/message/read/{id}")
  public String message(@PathVariable("action") String action, @PathVariable("id") String id, Model model) {
    messageService.read(id);
    return "redirect:/"+action+"/readMessage";
  }

}
