package com.foomei.core.web.api;

import com.foomei.common.dto.PageQuery;
import io.swagger.annotations.*;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foomei.common.dto.ErrorCodeFactory;
import com.foomei.common.dto.ResponseResult;
import com.foomei.common.mapper.BeanMapper;
import com.foomei.core.dto.TreeNodeDto;
import com.foomei.core.dto.UserGroupDto;
import com.foomei.core.entity.Role;
import com.foomei.core.entity.UserGroup;
import com.foomei.core.service.UserGroupService;
import com.foomei.core.service.UserService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Api(description = "机构接口")
@RestController
@RequestMapping(value = "/api/userGroup")
public class UserGroupEndpoint {

    @Autowired
    private UserService userService;
    @Autowired
    private UserGroupService userGroupService;

    static {
        Map<String, String> mapFields = Maps.newHashMap();
        mapFields.put("roleList{id}", "roleIds{}");
        BeanMapper.registerClassMap(UserGroup.class, UserGroupDto.class, mapFields);
    }

    @ApiOperation(value = "根据父节点ID获取机构列表", httpMethod = "POST", produces = "application/json")
    @RequiresRoles("admin")
    @RequestMapping(value = "tree")
    public List<TreeNodeDto> tree(Long id) {
        if (id != null) {
            List<UserGroup> userGroups = userGroupService.findChildrenByParent(id);
            return toNodes(userGroups);
        } else {
            List<UserGroup> userGroups = userGroupService.getAll();
            return toNodes(userGroups);
        }
    }

    @ApiOperation(value = "机构分页列表", httpMethod = "GET", produces = "application/json")
    @RequestMapping(value = "page")
    public ResponseResult<Page<UserGroupDto>> page(PageQuery pageQuery, Long parentId, HttpServletRequest request) {
        Page<UserGroup> page = userGroupService.getPage(pageQuery.getSearchKey(), parentId, pageQuery.buildPageRequest());
        return ResponseResult.createSuccess(page, UserGroup.class, UserGroupDto.class);
    }

    @ApiOperation(value = "机构新增", httpMethod = "POST", produces = "application/json")
    @RequiresRoles("admin")
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public ResponseResult create(@Valid @ModelAttribute UserGroup userGroup, BindingResult result,
                                 @RequestParam(value = "roles", required = false) List<Long> checkedRoles) {
        if (userGroupService.existCode(userGroup.getId(), userGroup.getCode())) {
            result.addError(new FieldError("userGroup", "code", "编码已经被使用"));
        }

        if (checkedRoles != null) {
            for (Long roleId : checkedRoles) {
                Role role = new Role(roleId);
                userGroup.getRoleList().add(role);
            }
        }

        UserGroup parent = null;
        if (userGroup.getParentId() != null) {
            parent = userGroupService.get(userGroup.getParentId());
            if (parent != null) {
                userGroup.setLevel(parent.getLevel() + 1);
            } else {
                result.addError(new FieldError("userGroup", "parentId", "上一级不存在"));
            }
        } else {
            userGroup.setLevel(1);
        }

        if (parent != null) {
            userGroup.setPath(parent.getPath() + UserGroup.PATH_SPLIT + userGroup.getCode());
        } else {
            userGroup.setPath(UserGroup.PATH_SPLIT + userGroup.getCode());
        }

        if (result.hasErrors()) {
            return ResponseResult.createParamError(result);
        } else {
            userGroupService.save(userGroup);
        }

        return ResponseResult.SUCCEED;
    }

    @ApiOperation(value = "机构修改", httpMethod = "POST", produces = "application/json")
    @RequiresRoles("admin")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public ResponseResult update(@Valid @ModelAttribute("preloadUserGroup") UserGroup userGroup, BindingResult result,
                                 @RequestParam(value = "roles", required = false) List<Long> checkedRoles) {
        if (userGroupService.existCode(userGroup.getId(), userGroup.getCode())) {
            result.addError(new FieldError("userGroup", "code", "编码已经被使用"));
        }

        userGroup.getRoleList().clear();
        if (checkedRoles != null) {
            for (Long roleId : checkedRoles) {
                Role role = new Role(roleId);
                userGroup.getRoleList().add(role);
            }
        }

        if (result.hasErrors()) {
            return ResponseResult.createParamError(result);
        } else {
            userGroupService.save(userGroup);
        }

        return ResponseResult.SUCCEED;
    }

    @ApiOperation(value = "机构删除", httpMethod = "GET", produces = "application/json")
    @RequiresRoles("admin")
    @RequestMapping(value = "delete/{id}")
    public ResponseResult delete(@PathVariable("id") Long id) {
        if (userService.existGroup(id)) {
            return ResponseResult.createError(ErrorCodeFactory.BAD_REQUEST, "请先删除此机构下的用户");
        }

        userGroupService.flagDelete(id);
        return ResponseResult.SUCCEED;
    }

    @ApiOperation(value = "机构获取", httpMethod = "GET", produces = "application/json")
    @RequiresRoles("admin")
    @RequestMapping(value = "get/{id}")
    public ResponseResult get(@PathVariable("id") Long id) {
        UserGroup userGroup = userGroupService.get(id);
        return ResponseResult.createSuccess(userGroup, UserGroupDto.class);
    }

    /**
     * 判断编码的唯一性
     */
    @ApiOperation(value = "检查机构编码是否存在", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "code", value = "编码", required = true, dataType = "string", paramType = "query")
    })
    @RequestMapping("checkCode")
    public boolean checkCode(Long id, String code) {
        return !userGroupService.existCode(id, code);
    }

    /**
     * 使用@ModelAttribute, 实现Struts2 Preparable二次部分绑定的效果,先根据form的id从数据库查出对象,再把Form提交的内容绑定到该对象上。
     * 因为仅update()方法的form中有id属性，因此本方法在该方法中执行.
     */
    @ModelAttribute("preloadUserGroup")
    public UserGroup getUserGroup(@RequestParam(value = "id", required = false) Long id) {
        if (id != null) {
            return userGroupService.get(id);
        }
        return null;
    }

    private List<TreeNodeDto> toNodes(List<UserGroup> userGroups) {
        List<TreeNodeDto> treeNodes = Lists.newArrayListWithCapacity(userGroups.size());
        for (UserGroup userGroup : userGroups) {
            treeNodes.add(new TreeNodeDto(userGroup));
        }
        return treeNodes;
    }

}
