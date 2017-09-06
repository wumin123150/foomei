package com.foomei.core.web.api;

import com.foomei.common.base.annotation.LogIgnore;
import com.foomei.common.dto.PageQuery;
import com.foomei.common.dto.ResponseResult;
import com.foomei.common.mapper.JsonMapper;
import com.foomei.common.persistence.JqGridFilter;
import com.foomei.core.dto.LogDto;
import com.foomei.core.entity.Log;
import com.foomei.core.service.LogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Api(description = "日志接口")
@RestController
@RequestMapping(value = "/api/log")
public class LogEndpoint {
	
	@Autowired
	private LogService logService;
	
	@ApiOperation(value = "日志分页列表", httpMethod = "GET", produces = "application/json")
	@ApiImplicitParams({
		@ApiImplicitParam(name = "startTime", value = "开始时间(yyyy-MM-dd HH:mm)", dataType = "date", paramType = "query"),
		@ApiImplicitParam(name = "endTime", value = "结束时间(yyyy-MM-dd HH:mm)", dataType = "date", paramType = "query")
	})
	@LogIgnore
	@RequiresRoles("admin")
	@RequestMapping(value = "page")
	public ResponseResult<Page<LogDto>> page(PageQuery pageQuery,
			@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date startTime, @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm") Date endTime,
			HttpServletRequest request) {
		Page<Log> page = null;
		if(pageQuery.getAdvance()) {
			JqGridFilter jqGridFilter = JsonMapper.nonDefaultMapper().fromJson(request.getParameter("filters"), JqGridFilter.class);
			page = logService.getPage(jqGridFilter, pageQuery.buildPageRequest());
		} else {
			page = logService.getPage(pageQuery.getSearchKey(), startTime, endTime, pageQuery.buildPageRequest("logTime", "desc"));
		}
		return ResponseResult.createSuccess(page, Log.class, LogDto.class);
	}

}
