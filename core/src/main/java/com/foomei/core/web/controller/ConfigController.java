package com.foomei.core.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

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

import com.foomei.core.dto.ConfigDto;
import com.foomei.core.dto.ConfigListDto;
import com.foomei.core.entity.Config;
import com.foomei.core.service.ConfigService;

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

    @ApiOperation(value = "系统配置新增", httpMethod = "POST")
    @RequiresRoles("admin")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(@Valid Config config, BindingResult result, Model model,
            RedirectAttributes redirectAttributes) {
        if (configService.existCode(config.getId(), config.getCode())) {
            result.addError(new FieldError("config", "code", "键已经被使用"));
        }

        if (result.hasErrors()) {
            model.addAttribute("menu", MENU);
            model.addAttribute("action", ACTION_CREATE);

            model.addAttribute("config", config);

            model.addAttribute("errors", result);
            return "admin/config/configForm";
        } else
            configService.save(config);

        redirectAttributes.addFlashAttribute("message", "新增系统配置成功");
        return "redirect:/admin/config";
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

    @ApiOperation(value = "系统配置修改", httpMethod = "POST")
    @RequiresRoles("admin")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public String update(@Valid @ModelAttribute("preloadConfig") Config config, BindingResult result,
            Model model, RedirectAttributes redirectAttributes) {
        if (configService.existCode(config.getId(), config.getCode())) {
            result.addError(new FieldError("config", "code", "键已经被使用"));
        }

        if (result.hasErrors()) {
            model.addAttribute("menu", MENU);
            model.addAttribute("action", ACTION_UPDATE);

            model.addAttribute("config", config);

            model.addAttribute("errors", result);
            return "admin/config/configForm";
        } else
            configService.save(config);

        redirectAttributes.addFlashAttribute("message", "保存系统配置成功");
        return "redirect:/admin/config";
    }
    
    @ApiOperation(value = "系统配置查看页面", httpMethod = "GET")
    @RequiresRoles("admin")
    @RequestMapping(value = "view", method = RequestMethod.GET)
    public String viewForm(Model model) {
        model.addAttribute("menu", "configs");

        model.addAttribute("configs", configService.getAll());
        return "admin/config/view";
    }
    
    @ApiOperation(value = "参数修改", httpMethod = "POST")
    @RequiresRoles("admin")
    @RequestMapping(value = "updateAll", method = RequestMethod.POST)
    public String updateAll(ConfigListDto configList, Model model, RedirectAttributes redirectAttributes) {
        List<ConfigDto> configs = configList.getConfigs();
        if(configs != null) {
            for (ConfigDto configDto : configs) {
                Config config = configService.get(configDto.getId());
                if(config != null) {
                    config.setValue(configDto.getValue());
                    configService.save(config);
                }
            }
        }

        redirectAttributes.addFlashAttribute("message", "保存参数成功");
        return "redirect:/admin/config/configView";
    }

    @ApiOperation(value = "系统配置删除", httpMethod = "GET")
    @RequiresRoles("admin")
    @RequestMapping(value = "delete/{id}")
    public String delete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        configService.delete(id);
        redirectAttributes.addFlashAttribute("message", "删除系统配置成功");
        return "redirect:/admin/config";
    }

    /**
     * 使用@ModelAttribute, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
     */
    @ModelAttribute("preloadConfig")
    public Config getConfig(@RequestParam(value = "id", required = false) Long id) {
        if (id != null) {
            return configService.get(id);
        }
        return null;
    }

}
