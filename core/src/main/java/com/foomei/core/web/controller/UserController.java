package com.foomei.core.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.foomei.common.web.Servlets;
import com.foomei.core.entity.Annex;
import com.foomei.core.entity.Role;
import com.foomei.core.entity.User;
import com.foomei.core.entity.UserGroup;
import com.foomei.core.service.AnnexService;
import com.foomei.core.service.DataDictionaryService;
import com.foomei.core.service.RoleService;
import com.foomei.core.service.UserGroupService;
import com.foomei.core.service.UserService;

/**
 * 管理员管理用户的Controller.
 *
 * @author walker
 */
@Api(description = "用户管理")
@Controller
@RequestMapping(value = "/admin/user")
public class UserController {

  private static Logger logger = LoggerFactory.getLogger(UserController.class);

  private static final String MENU = "user";
  private static final String ACTION_CREATE = "create";
  private static final String ACTION_UPDATE = "update";

  @Value("${upload.folder:/opt/upload/}")
  private String root;

  @Autowired
  private UserService userService;
  @Autowired
  private RoleService roleService;
  @Autowired
  private UserGroupService userGroupService;
  @Autowired
  private AnnexService annexService;
  @Autowired
  private DataDictionaryService dataDictionaryService;

  @ApiOperation(value = "用户列表页面", httpMethod = "GET")
  @RequiresRoles("admin")
  @RequestMapping
  public String list(Model model) {
    model.addAttribute("menu", MENU);
    return "admin/user/userList";
  }

  @ApiOperation(value = "用户新增页面", httpMethod = "GET")
  @RequiresRoles("admin")
  @RequestMapping(value = "create", method = RequestMethod.GET)
  public String createForm(Model model) {
    model.addAttribute("menu", MENU);
    model.addAttribute("action", ACTION_CREATE);

    model.addAttribute("user", new User());
    model.addAttribute("roles", roleService.getAll());
    return "admin/user/userForm";
  }

  @ApiOperation(value = "用户新增", httpMethod = "POST")
  @RequiresRoles("admin")
  @RequestMapping(value = "create", method = RequestMethod.POST)
  public String create(@Valid User user, BindingResult result,
                       @RequestParam(value = "roles", required = false) List<Long> checkedRoles,
                       @RequestParam(value = "groups", required = false) List<Long> checkedGroups,
                       @RequestParam MultipartFile file,
                       Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
    if (checkedRoles != null) {
      for (Long roleId : checkedRoles) {
        Role role = new Role(roleId);
        user.getRoleList().add(role);
      }
    }

    if (checkedGroups != null) {
      for (Long groupId : checkedGroups) {
        UserGroup userGroup = new UserGroup(groupId);
        user.getGroupList().add(userGroup);
      }
    }

    if (userService.existLoginName(user.getId(), user.getLoginName())) {
      result.addError(new FieldError("user", "loginName", "账号已经被使用"));
    }

    if (result.hasErrors()) {
      model.addAttribute("menu", MENU);
      model.addAttribute("action", ACTION_CREATE);

      model.addAttribute("user", user);
      model.addAttribute("roles", roleService.getAll());

      model.addAttribute("errors", result);
      return "admin/user/userForm";
    }

    user.setRegisterTime(new Date());
    user.setRegisterIp(Servlets.getIpAddress(request));

    userService.save(user);

    if (file != null && !file.isEmpty()) {
      try {
        Annex annex = annexService.save(file.getBytes(), file.getOriginalFilename(), User.USER_ANNEX_PATH, String.valueOf(user.getId()), User.USER_ANNEX_TYPE);
        if (annex != null) {
          user.setAvatar(annex.getPath());
        }
      } catch (Exception e) {
        logger.error("存储头像附件失败", e);
      }
    }

    redirectAttributes.addFlashAttribute("message", "新增用户成功");
    return "redirect:/admin/user";
  }

  @ApiOperation(value = "用户修改页面", httpMethod = "GET")
  @RequiresRoles("admin")
  @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
  public String updateForm(@PathVariable("id") Long id, Model model) {
    model.addAttribute("menu", MENU);
    model.addAttribute("action", ACTION_UPDATE);

    model.addAttribute("user", userService.get(id));
    model.addAttribute("roles", roleService.getAll());
    return "admin/user/userForm";
  }

  /**
   * 演示自行绑定表单中的checkBox roleList到对象中.
   */
  @ApiOperation(value = "用户修改", httpMethod = "POST")
  @RequiresRoles("admin")
  @RequestMapping(value = "update", method = RequestMethod.POST)
  public String update(@Valid @ModelAttribute("preloadUser") User user, BindingResult result,
                       @RequestParam(value = "roles", required = false) List<Long> checkedRoles,
                       @RequestParam(value = "groups", required = false) List<Long> checkedGroups,
                       @RequestParam MultipartFile file,
                       Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {
    //bind roles
    user.getRoleList().clear();
    if (checkedRoles != null) {
      for (Long roleId : checkedRoles) {
        Role role = new Role(roleId);
        user.getRoleList().add(role);
      }
    }

    user.getGroupList().clear();
    if (checkedGroups != null) {
      for (Long groupId : checkedGroups) {
        UserGroup userGroup = new UserGroup(groupId);
        user.getGroupList().add(userGroup);
      }
    }

    if (file != null && !file.isEmpty()) {
      try {
        Annex annex = annexService.save(file.getBytes(), file.getOriginalFilename(), User.USER_ANNEX_PATH, String.valueOf(user.getId()), User.USER_ANNEX_TYPE);
        if (annex != null) {
          user.setAvatar(annex.getPath());
        }
      } catch (Exception e) {
        logger.error("存储头像附件失败", e);
      }
    }

    if (userService.existLoginName(user.getId(), user.getLoginName())) {
      result.addError(new FieldError("user", "loginName", "账号已经被使用"));
    }

    if (result.hasErrors()) {
      model.addAttribute("menu", MENU);
      model.addAttribute("action", ACTION_UPDATE);

      model.addAttribute("user", user);
      model.addAttribute("roles", roleService.getAll());

      model.addAttribute("errors", result);
      return "admin/user/userForm";
    } else
      userService.save(user);

    redirectAttributes.addFlashAttribute("message", "保存用户成功");
    return "redirect:/admin/user";
  }

  @ApiOperation(value = "重置密码页面", httpMethod = "GET")
  @RequiresRoles("admin")
  @RequestMapping(value = "reset/{id}", method = RequestMethod.GET)
  public String resetForm(@PathVariable("id") Long id, Model model) {
    model.addAttribute("menu", MENU);

    model.addAttribute("user", userService.get(id));
    return "admin/user/resetPassword";
  }

  @ApiOperation(value = "密码重置", httpMethod = "POST")
  @RequiresRoles("admin")
  @RequestMapping(value = "reset", method = RequestMethod.POST)
  public String reset(String loginName, String plainPassword, Model model, RedirectAttributes redirectAttributes) {
    ObjectError error = null;
    if (StringUtils.isEmpty(plainPassword)) {
      error = new ObjectError("password", "密码不能为空");
    }

    if (error != null) {
      model.addAttribute("menu", MENU);

      model.addAttribute("user", userService.getByLoginName(loginName));

      model.addAttribute("error", error);
      return "admin/user/resetPassword";
    } else
      userService.changePassword(loginName, plainPassword);

    redirectAttributes.addFlashAttribute("message", "重置密码成功");
    return "redirect:/admin/user";
  }

  @ApiOperation(value = "用户停用", httpMethod = "GET")
  @RequiresRoles("admin")
  @RequestMapping(value = "delete/{id}")
  public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
    userService.delete(id);
    redirectAttributes.addFlashAttribute("message", "停用账号成功");
    return "redirect:/admin/user";
  }

  /**
   * 使用@ModelAttribute, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出User对象,再把Form提交的内容绑定到该对象上。
   * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
   */
  @ModelAttribute("preloadUser")
  public User getUser(@RequestParam(value = "id", required = false) Long id) {
    if (id != null) {
      return userService.get(id);
    }
    return null;
  }

  /**
   * 不自动绑定对象中的roleList属性，另行处理。
   */
  @InitBinder
  protected void initBinder(WebDataBinder binder) {
    binder.setDisallowedFields("roleList");
  }

}
