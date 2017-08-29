package com.foomei.core.web.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.foomei.common.dto.ResponseResult;
import com.foomei.core.entity.Area;
import com.foomei.core.service.AreaService;
import com.google.common.collect.Lists;

@Api(description = "地区接口")
@RestController
@RequestMapping(value = "/api/area")
public class AreaEndpoint {

	@Autowired
	private AreaService areaService;

	@ApiOperation(value = "根据父节点ID获取地区列表", httpMethod = "GET", produces = "application/json")
	@RequestMapping(value = "findByParent")
	public ResponseResult<List<Area>> findByParent(String parentId) {
		List<Area> areas = Lists.newArrayList();
		if (StringUtils.isEmpty(parentId)) {
			areas = areaService.findTop();
		} else {
			areas = areaService.findChildrenByParent(parentId);
		}
		return ResponseResult.createSuccess(areas);
	}

	@ApiOperation(value = "获取省列表", httpMethod = "GET", produces = "application/json")
	@RequestMapping(value = "province", method = RequestMethod.GET)
	public ResponseResult<List<Area>> province() {
		return ResponseResult.createSuccess(areaService.findTop());
	}

	@ApiOperation(value = "获取市列表", httpMethod = "GET", produces = "application/json")
	@RequestMapping(value = "city", method = RequestMethod.GET)
	public ResponseResult<List<Area>> city(@ApiParam(value="省ID", required=true) String parentId) {
		return ResponseResult.createSuccess(areaService.findChildrenByParent(parentId));
	}

	@ApiOperation(value = "获取县列表", httpMethod = "GET", produces = "application/json")
	@RequestMapping(value = "county", method = RequestMethod.GET)
	public ResponseResult<List<Area>> county(@ApiParam(value="市ID", required=true) String parentId) {
		return ResponseResult.createSuccess(areaService.findChildrenByParent(parentId));
	}

	@ApiOperation(value = "获取城市列表", httpMethod = "GET", produces = "application/json")
	@RequestMapping(value = "town", method = RequestMethod.GET)
	public ResponseResult<List<Area>> town(@ApiParam(value="省ID", required=true) String rootId) {
		return ResponseResult.createSuccess(areaService.findTownByRoot(rootId));
	}

	@ApiOperation(value = "地区获取", httpMethod = "GET", produces = "application/json")
	@RequestMapping(value = "get/{id}")
	public ResponseResult<Area> get(@PathVariable("id") String id) {
		return ResponseResult.createSuccess(areaService.get(id));
	}

}
