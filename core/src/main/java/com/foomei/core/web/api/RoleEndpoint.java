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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foomei.common.dto.ResponseResult;
import com.foomei.common.mapper.JsonMapper;
import com.foomei.common.persistence.JqGridFilter;
import com.foomei.common.persistence.SearchFilter;
import com.foomei.core.dto.RoleDto;
import com.foomei.core.entity.Role;
import com.foomei.core.service.RoleService;

@Api(description = "角色接口")
@RestController
@RequestMapping(value = "/api/role")
public class RoleEndpoint {
	
	private static final String PAGE_SIZE = "10";
	
	@Autowired
	private RoleService roleService;
	
	@ApiOperation(value = "角色分页列表", httpMethod = "GET", produces = "application/json")
	@RequiresRoles("admin")
	@RequestMapping(value = "page")
	public ResponseResult<Page<RoleDto>> page(@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = PAGE_SIZE) int pageSize, 
			@RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "desc") String sortDir, 
			@RequestParam(defaultValue = "false") Boolean advance, String searchKey, HttpServletRequest request) {
		PageRequest pageRequest = buildPageRequest(pageNo, pageSize, sortBy, sortDir);
		Page<Role> page = null;
		if(advance) {
			JqGridFilter jqGridFilter = JsonMapper.nonDefaultMapper().fromJson(request.getParameter("filters"), JqGridFilter.class);
			page = roleService.getPage(jqGridFilter, pageRequest);
		} else {
		    SearchFilter searchFilter = new SearchFilter().or()
		            .addLike(Role.PROP_CODE, searchKey)
		            .addLike(Role.PROP_NAME, searchKey);
			page = roleService.getPage(searchFilter, pageRequest);
		}
		return ResponseResult.createSuccess(page, Role.class, RoleDto.class);
	}
	
	/**
	 * 判断编码的唯一性
	 * 
	 */
	@ApiOperation(value = "检查角色编码是否存在", httpMethod = "GET")
	@RequestMapping("checkCode")
	public boolean checkCode(Long id, @ApiParam(value="编码", required=true)String code) {
		return !roleService.existCode(id, code);
	}

	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNo, int pageSize, String sortBy, String sortDir) {
		return new PageRequest(pageNo - 1, pageSize, Direction.fromString(sortDir), sortBy);
	}

}
