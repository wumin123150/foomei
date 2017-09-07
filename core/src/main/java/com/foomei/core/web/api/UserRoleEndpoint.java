package com.foomei.core.web.api;

import com.foomei.common.dto.PageQuery;
import com.foomei.common.dto.ResponseResult;
import com.foomei.core.entity.BaseUser;
import com.foomei.core.entity.Role;
import com.foomei.core.entity.User;
import com.foomei.core.service.RoleService;
import com.foomei.core.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Api(description = "角色授权接口")
@RestController
@RequestMapping(value = "/api/userRole")
public class UserRoleEndpoint {

  @Autowired
  private UserService userService;
  @Autowired
  private RoleService roleService;

  @ApiOperation(value = "角色授权分页列表", httpMethod = "GET", produces = "application/json")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "roleId", value = "角色ID", dataType = "long", paramType = "query")
  })
  @RequiresRoles("admin")
  @RequestMapping(value = "page")
  public ResponseResult<Page<BaseUser>> page(PageQuery pageQuery, Long roleId, HttpServletRequest request) {
    Page<User> page = userService.getPageByRole(pageQuery.getSearchKey(), roleId, pageQuery.buildPageRequest());
    return ResponseResult.createSuccess(page, User.class, BaseUser.class);
  }

  @ApiOperation(value = "角色授权新增", httpMethod = "POST", produces = "application/json")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "long", paramType = "form"),
    @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "long", paramType = "form")
  })
  @RequiresRoles("admin")
  @RequestMapping(value = "create", method = RequestMethod.POST)
  public ResponseResult create(Long userId, Long roleId) {
    User user = userService.get(userId);
    Role role = roleService.get(roleId);
    user.getRoleList().add(role);
    userService.save(user);
    return ResponseResult.SUCCEED;
  }

  @ApiOperation(value = "角色授权删除", httpMethod = "POST", produces = "application/json")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "long", paramType = "form"),
    @ApiImplicitParam(name = "roleId", value = "角色ID", required = true, dataType = "long", paramType = "form")
  })
  @RequiresRoles("admin")
  @RequestMapping(value = "delete")
  public ResponseResult delete(Long userId, Long roleId) {
    User user = userService.get(userId);
    for (Role role : user.getRoleList()) {
      if (role.getId().equals(roleId)) {
        user.getRoleList().remove(role);
        break;
      }
    }
    userService.save(user);
    return ResponseResult.SUCCEED;
  }

}
