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
import com.foomei.core.entity.User;
import com.foomei.core.entity.UserGroup;
import com.foomei.core.service.UserGroupService;
import com.foomei.core.service.UserService;

@Api(description = "机构授权接口")
@RestController
@RequestMapping(value = "/api/membership")
public class MembershipEndpoint {
	
	private static final String PAGE_SIZE = "10";
	
	@Autowired
	private UserService userService;
	@Autowired
	private UserGroupService userGroupService;

	@ApiOperation(value = "机构授权分页列表", httpMethod = "GET", produces = "application/json")
	@RequiresRoles("admin")
	@RequestMapping(value = "page")
	public ResponseResult<Page<BaseUser>> page(@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = PAGE_SIZE) int pageSize, 
			@RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "desc") String sortDir, 
			@RequestParam(defaultValue = "false") Boolean advance, String searchKey, 
			Long groupId, HttpServletRequest request) {
		PageRequest pageRequest = buildPageRequest(pageNo, pageSize, sortBy, sortDir);
		Page<User> page = userService.getPageByGroup(searchKey, groupId, pageRequest);
		return ResponseResult.createSuccess(page, User.class, BaseUser.class);
	}

	@ApiOperation(value = "机构授权新增", httpMethod = "POST", produces = "application/json")
	@RequiresRoles("admin")
	@RequestMapping(value = "create", method = RequestMethod.POST)
	public ResponseResult create(@ApiParam(value="用户ID", required=true)Long userId, @ApiParam(value="组ID", required=true)Long groupId) {
		User user = userService.get(userId);
		UserGroup group = userGroupService.get(groupId);
		user.getGroupList().add(group);
		userService.save(user);
		return ResponseResult.SUCCEED;
	}

	@ApiOperation(value = "机构授权删除", httpMethod = "POST", produces = "application/json")
	@RequiresRoles("admin")
	@RequestMapping(value = "delete")
	public ResponseResult delete(@ApiParam(value="用户ID", required=true)Long userId, @ApiParam(value="组ID", required=true)Long groupId) {
		User user = userService.get(userId);
		for (UserGroup group : user.getGroupList()) {
			if(group.getId().equals(groupId)) {
				user.getGroupList().remove(group);
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
