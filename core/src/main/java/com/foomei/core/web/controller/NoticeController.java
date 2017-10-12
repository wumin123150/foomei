package com.foomei.core.web.controller;

import com.foomei.common.collection.CollectionExtractor;
import com.foomei.common.collection.ListUtil;
import com.foomei.core.entity.BaseUser;
import com.foomei.core.entity.Notice;
import com.foomei.core.entity.NoticeReceive;
import com.foomei.core.service.NoticeReceiveService;
import com.foomei.core.service.NoticeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Api(description = "通知管理")
@Controller
@RequestMapping(value = "/admin/notice")
public class NoticeController {

  private static final String MENU = "notice";
  private static final String ACTION_CREATE = "create";
  private static final String ACTION_UPDATE = "update";

  @Autowired
  private NoticeService noticeService;
  @Autowired
  private NoticeReceiveService noticeReceiveService;

  @ApiOperation(value = "通知列表页面", httpMethod = "GET")
  @RequiresRoles("admin")
  @RequestMapping
  public String list(Model model) {
    model.addAttribute("menu", MENU);
    return "admin/notice/noticeList";
  }

  @ApiOperation(value = "通知新增页面", httpMethod = "GET")
  @RequiresRoles("admin")
  @RequestMapping(value = "create", method = RequestMethod.GET)
  public String createForm(Model model) {
    model.addAttribute("menu", MENU);
    model.addAttribute("action", ACTION_CREATE);

    model.addAttribute("notice", new Notice());
    return "admin/notice/noticeForm";
  }

  @ApiOperation(value = "通知新增", httpMethod = "POST")
  @RequiresRoles("admin")
  @RequestMapping(value = "create", method = RequestMethod.POST)
  public String create(@Valid Notice notice, BindingResult result, @RequestParam(value = "users") List<Long> checkedUsers, Model model, RedirectAttributes redirectAttributes) {
    if (result.hasErrors()) {
      model.addAttribute("menu", MENU);
      model.addAttribute("action", ACTION_CREATE);

      model.addAttribute("notice", notice);

      model.addAttribute("errors", result);
      return "admin/notice/noticeForm";
    } else
      noticeService.save(notice, checkedUsers);

    redirectAttributes.addFlashAttribute("message", "新增通知成功");
    return "redirect:/admin/notice";
  }

  @ApiOperation(value = "通知修改页面", httpMethod = "GET")
  @RequiresRoles("admin")
  @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
  public String updateForm(@PathVariable("id") String id, Model model) {
    model.addAttribute("menu", MENU);
    model.addAttribute("action", ACTION_UPDATE);

    List<BaseUser> users = ListUtil.newArrayList();

    Notice notice = noticeService.get(id);
    List<NoticeReceive> noticeReceives = noticeReceiveService.findByNotice(id);
    for(NoticeReceive noticeReceive : noticeReceives) {
      users.add(noticeReceive.getUser());
    }

    model.addAttribute("notice", notice);
    model.addAttribute("userIds", CollectionExtractor.extractToString(users, BaseUser.PROP_ID, ","));
    model.addAttribute("userNames", CollectionExtractor.extractToString(users, BaseUser.PROP_NAME, ","));
    return "admin/notice/noticeForm";
  }

  @ApiOperation(value = "通知修改", httpMethod = "POST")
  @RequiresRoles("admin")
  @RequestMapping(value = "update", method = RequestMethod.POST)
  public String update(@Valid @ModelAttribute("preloadNotice") Notice notice, BindingResult result, @RequestParam(value = "users") List<Long> checkedUsers,
                       Model model, RedirectAttributes redirectAttributes) {
    if (result.hasErrors()) {
      model.addAttribute("menu", MENU);
      model.addAttribute("action", ACTION_UPDATE);

      model.addAttribute("notice", notice);

      model.addAttribute("errors", result);
      return "admin/notice/noticeForm";
    } else
      noticeService.save(notice, checkedUsers);

    redirectAttributes.addFlashAttribute("message", "保存通知成功");
    return "redirect:/admin/notice";
  }

  /**
   * 使用@ModelAttribute, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出对象,再把Form提交的内容绑定到该对象上。
   * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
   */
  @ModelAttribute("preloadNotice")
  public Notice getNotice(@RequestParam(value = "id", required = false) String id) {
    if (id != null) {
      return noticeService.get(id);
    }
    return null;
  }

}
