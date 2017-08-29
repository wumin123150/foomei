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
import com.foomei.common.mapper.JsonMapper;
import com.foomei.common.persistence.JqGridFilter;
import com.foomei.common.persistence.SearchFilter;
import com.foomei.core.entity.DataType;
import com.foomei.core.service.DataTypeService;

@Api(description = "数据类型接口")
@RestController
@RequestMapping(value = "/api/dataType")
public class DataTypeEndpoint {

	private static final String PAGE_SIZE = "10";

	@Autowired
	private DataTypeService dataTypeService;

	@ApiOperation(value = "数据类型分页列表", httpMethod = "GET", produces = "application/json")
	@RequiresRoles("admin")
	@RequestMapping(value = "page", method = RequestMethod.GET)
	public ResponseResult<Page<DataType>> page(@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = PAGE_SIZE) int pageSize, 
			@RequestParam(defaultValue = "id") String sortBy, @RequestParam(defaultValue = "desc") String sortDir, 
			@RequestParam(defaultValue = "false") Boolean advance, String searchKey, HttpServletRequest request) {
		PageRequest pageRequest = buildPageRequest(pageNo, pageSize, sortBy, sortDir);
		Page<DataType> page = null;
		if(advance) {
			JqGridFilter jqGridFilter = JsonMapper.nonDefaultMapper().fromJson(request.getParameter("filters"), JqGridFilter.class);
			page = dataTypeService.getPage(jqGridFilter, pageRequest);
		} else {
		    SearchFilter searchFilter = new SearchFilter().or()
		            .addLike(DataType.PROP_CODE, searchKey)
		            .addLike(DataType.PROP_NAME, searchKey);
			page = dataTypeService.getPage(searchFilter, pageRequest);
		}

		return ResponseResult.createSuccess(page);
	}
	
	/**
	 * 判断编码的唯一性
	 * 
	 */
	@ApiOperation(value = "检查数据类型编码是否存在", httpMethod = "GET")
	@RequestMapping("checkCode")
	public boolean checkCode(Long id, @ApiParam(value="编码", required=true)String code) {
		return !dataTypeService.existCode(id, code);
	}
	
	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNo, int pageSize, String sortBy, String sortDir) {
		return new PageRequest(pageNo - 1, pageSize, Direction.fromString(sortDir), sortBy);
	}
	
}
