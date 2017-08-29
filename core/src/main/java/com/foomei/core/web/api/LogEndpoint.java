package com.foomei.core.web.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foomei.common.base.annotation.LogIgnore;
import com.foomei.common.dto.ResponseResult;
import com.foomei.common.mapper.JsonMapper;
import com.foomei.common.persistence.JqGridFilter;
import com.foomei.core.dto.LogDto;
import com.foomei.core.entity.Log;
import com.foomei.core.service.LogService;

@Api(description = "日志接口")
@RestController
@RequestMapping(value = "/api/log")
public class LogEndpoint {
	
	private static final String PAGE_SIZE = "10";
	
	@Autowired
	private LogService logService;
	
	@ApiOperation(value = "日志分页列表", httpMethod = "GET", produces = "application/json")
	@LogIgnore
	@RequiresRoles("admin")
	@RequestMapping(value = "page")
	public ResponseResult<Page<LogDto>> page(@RequestParam(defaultValue = "1") int pageNo, @RequestParam(defaultValue = PAGE_SIZE) int pageSize, 
			@RequestParam(defaultValue = "logTime") String sortBy, @RequestParam(defaultValue = "desc") String sortDir, 
			@RequestParam(defaultValue = "false") Boolean advance, String searchKey, 
			@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date startTime, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date endTime, 
			HttpServletRequest request) {
		PageRequest pageRequest = buildPageRequest(pageNo, pageSize, sortBy, sortDir);
		Page<Log> page = null;
		if(advance) {
			JqGridFilter jqGridFilter = JsonMapper.nonDefaultMapper().fromJson(request.getParameter("filters"), JqGridFilter.class);
			page = logService.getPage(jqGridFilter, pageRequest);
		} else {
			page = logService.getPage(searchKey, startTime, endTime, pageRequest);
		}
		return ResponseResult.createSuccess(page, Log.class, LogDto.class);
	}
	

	/**
	 * 创建分页请求.
	 */
	private PageRequest buildPageRequest(int pageNo, int pageSize, String sortBy, String sortDir) {
		return new PageRequest(pageNo - 1, pageSize, Direction.fromString(sortDir), sortBy);
	}

}
