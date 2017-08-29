package com.foomei.core.web.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foomei.common.dto.ResponseResult;
import com.foomei.core.entity.BaseUser;
import com.foomei.core.entity.Role;
import com.foomei.core.entity.User;
import com.foomei.core.service.RoleService;
import com.foomei.core.service.UserService;

@Api(description = "角色授权接口")
@RestController
@RequestMapping(value = "/api/userRole")
public class UserRoleEndpoint {
	
	private static final String PAGE_SIZE = "10";
	
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;

	@ApiOperation(value = "角色授权分页列表", httpMethod = "GET", produces = "application/json")
	@RequiresRoles("admin")
	@RequestMapping(value = "page")
	public ResponseResult<Page<BaseUser>> page(@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = PAGE_SIZE) int pageSize, 
			@RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "desc") String sortDir, 
			@RequestParam(defaultValue = "false") Boolean advance, String searchKey, 
			Long roleId, HttpServletRequest request) {
		PageRequest pageRequest = buildPageRequest(pageNo, pageSize, sortBy, sortDir);
		Page<User> page = userService.getPageByRole(searchKey, roleId, pageRequest);
		return ResponseResult.createSuccess(page, User.class, BaseUser.class);
	}

	@ApiOperation(value = "角色授权新增", httpMethod = "POST", produces = "application/json")
	@RequiresRoles("admin")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public ResponseResult create(@ApiParam(value="用户ID", required=true)Long userId, @ApiParam(value="角色ID", required=true)Long roleId) {
		User user = userService.get(userId);
		Role role = roleService.get(roleId);
		user.getRoleList().add(role);
		userService.save(user);
		return ResponseResult.SUCCEED;
	}

	@ApiOperation(value = "角色授权删除", httpMethod = "POST", produces = "application/json")
	@RequiresRoles("admin")
	@RequestMapping(value = "delete")
	public ResponseResult delete(@ApiParam(value="用户ID", required=true)Long userId, @ApiParam(value="角色ID", required=true)Long roleId) {
		User user = userService.get(userId);
		for (Role role : user.getRoleList()) {
			if(role.getId().equals(roleId)) {
				user.getRoleList().remove(role);
				break;
			}
		}
		userService.save(user);
		return ResponseResult.SUCCEED;
	}

	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNo, int pageSize, String sortBy, String sortDir) {
		return new PageRequest(pageNo - 1, pageSize, Direction.fromString(sortDir), sortBy);
	}

}
