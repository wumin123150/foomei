package com.foomei.core.web.api;

import com.foomei.common.dto.PageQuery;
import com.foomei.common.dto.ResponseResult;
import com.foomei.common.mapper.JsonMapper;
import com.foomei.common.persistence.JqGridFilter;
import com.foomei.common.persistence.SearchFilter;
import com.foomei.core.dto.DataTypeDto;
import com.foomei.core.entity.DataType;
import com.foomei.core.service.DataTypeService;
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

@Api(description = "数据类型接口")
@RestController
@RequestMapping(value = "/api/dataType")
public class DataTypeEndpoint {

  @Autowired
  private DataTypeService dataTypeService;

  @ApiOperation(value = "数据类型分页列表", httpMethod = "GET", produces = "application/json")
  @RequiresRoles("admin")
  @RequestMapping(value = "page", method = RequestMethod.GET)
  public ResponseResult<Page<DataTypeDto>> page(PageQuery pageQuery, HttpServletRequest request) {
    Page<DataType> page = null;
    if (pageQuery.getAdvance()) {
      JqGridFilter jqGridFilter = JsonMapper.INSTANCE.fromJson(request.getParameter("filters"), JqGridFilter.class);
      page = dataTypeService.getPage(jqGridFilter, pageQuery.buildPageRequest());
    } else {
      SearchFilter searchFilter = new SearchFilter().or()
        .addLike(DataType.PROP_CODE, pageQuery.getSearchKey())
        .addLike(DataType.PROP_NAME, pageQuery.getSearchKey());
      page = dataTypeService.getPage(searchFilter, pageQuery.buildPageRequest());
    }

    return ResponseResult.createSuccess(page, DataType.class, DataTypeDto.class);
  }

  /**
   * 判断编码的唯一性
   */
  @ApiOperation(value = "检查数据类型编码是否存在", httpMethod = "GET")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "code", value = "编码", required = true, dataType = "string", paramType = "query")
  })
  @RequestMapping("checkCode")
  public boolean checkCode(Long id, String code) {
    return !dataTypeService.existCode(id, code);
  }

}
