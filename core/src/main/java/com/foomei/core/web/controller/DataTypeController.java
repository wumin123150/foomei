package com.foomei.core.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.foomei.core.entity.DataType;
import com.foomei.core.service.DataTypeService;

@Api(description = "数据类型管理")
@Controller
@RequestMapping(value = "/admin/dataType")
public class DataTypeController {

  private static final String MENU = "dataDictionary";
  private static final String ACTION_CREATE = "create";
  private static final String ACTION_UPDATE = "update";

  @Autowired
  private DataTypeService dataTypeService;

  @ApiOperation(value = "数据类型列表页面", httpMethod = "GET")
  @RequiresRoles("admin")
  @RequestMapping
  public String list(Model model) {
    model.addAttribute("menu", MENU);
    return "admin/dataDictionary/dataTypeList";
  }

  @ApiOperation(value = "数据类型新增页面", httpMethod = "GET")
  @RequiresRoles("admin")
  @RequestMapping(value = "create", method = RequestMethod.GET)
  public String createForm(Model model) {
    model.addAttribute("menu", MENU);
    model.addAttribute("action", ACTION_CREATE);

    model.addAttribute("dataType", new DataType());
    return "admin/dataDictionary/dataTypeForm";
  }

  @ApiOperation(value = "数据类型新增", httpMethod = "POST")
  @RequiresRoles("admin")
  @RequestMapping(value = "create", method = RequestMethod.POST)
  public String create(@Valid DataType dataType, BindingResult result, Model model,
                       RedirectAttributes redirectAttributes) {
    if (dataTypeService.existCode(dataType.getId(), dataType.getCode())) {
      result.addError(new FieldError("dataType", "code", "编码已经被使用"));
    }

    if (result.hasErrors()) {
      model.addAttribute("menu", MENU);
      model.addAttribute("action", ACTION_CREATE);

      model.addAttribute("dataType", dataType);

      model.addAttribute("errors", result);
      return "admin/dataDictionary/dataTypeForm";
    } else
      dataTypeService.save(dataType);

    redirectAttributes.addFlashAttribute("message", "新增数据类型成功");
    return "redirect:/admin/dataType";
  }

  @ApiOperation(value = "数据类型修改页面", httpMethod = "GET")
  @RequiresRoles("admin")
  @RequestMapping(value = "update/{id}", method = RequestMethod.GET)
  public String updateForm(@PathVariable("id") Long id, Model model) {
    model.addAttribute("menu", MENU);
    model.addAttribute("action", ACTION_UPDATE);

    model.addAttribute("dataType", dataTypeService.get(id));
    return "admin/dataDictionary/dataTypeForm";
  }

  @ApiOperation(value = "数据类型修改", httpMethod = "POST")
  @RequiresRoles("admin")
  @RequestMapping(value = "update", method = RequestMethod.POST)
  public String update(@Valid @ModelAttribute("preloadDataType") DataType dataType, BindingResult result,
                       Model model, RedirectAttributes redirectAttributes) {
    if (dataTypeService.existCode(dataType.getId(), dataType.getCode())) {
      result.addError(new FieldError("dataType", "code", "编码已经被使用"));
    }

    if (result.hasErrors()) {
      model.addAttribute("menu", MENU);
      model.addAttribute("action", ACTION_UPDATE);

      model.addAttribute("dataType", dataType);

      model.addAttribute("errors", result);
      return "admin/dataType/dataTypeForm";
    } else
      dataTypeService.save(dataType);

    redirectAttributes.addFlashAttribute("message", "保存数据类型成功");
    return "redirect:/admin/dataType";
  }

  @ApiOperation(value = "数据类型删除", httpMethod = "GET")
  @RequiresRoles("admin")
  @RequestMapping(value = "delete/{id}")
  public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
    dataTypeService.delete(id);
    redirectAttributes.addFlashAttribute("message", "删除数据类型成功");
    return "redirect:/admin/dataType";
  }

  /**
   * 使用@ModelAttribute, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出对象,再把Form提交的内容绑定到该对象上。
   * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
   */
  @ModelAttribute("preloadDataType")
  public DataType getDataType(@RequestParam(value = "id", required = false) Long id) {
    if (id != null) {
      return dataTypeService.get(id);
    }
    return null;
  }

}
