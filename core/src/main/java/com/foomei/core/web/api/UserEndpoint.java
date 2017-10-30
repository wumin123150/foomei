package com.foomei.core.web.api;

import com.foomei.common.dto.PageQuery;
import com.foomei.common.dto.ResponseResult;
import com.foomei.common.mapper.JsonMapper;
import com.foomei.common.persistence.JqGridFilter;
import com.foomei.common.persistence.search.SearchRequest;
import com.foomei.core.entity.BaseUser;
import com.foomei.core.service.BaseUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(description = "用户接口")
@RestController
@RequestMapping(value = "/api/user")
public class UserEndpoint {

  @Autowired
  private BaseUserService baseUserService;

  @ApiOperation(value = "用户智能搜索", notes = "按名称和手机查询", httpMethod = "GET", produces = "application/json")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "q", value = "关键词, searchKey作废", required = true, dataType = "string", paramType = "query")
  })
  @RequestMapping(value = "search")
  public ResponseResult<Page<BaseUser>> search(PageQuery pageQuery, @RequestParam("q") String searchKey) {
    Page<BaseUser> users = baseUserService.getPage(new SearchRequest(pageQuery, new Sort(Sort.Direction.DESC, BaseUser.PROP_NAME), BaseUser.PROP_NAME, BaseUser.PROP_LOGIN_NAME, BaseUser.PROP_MOBILE));
    return ResponseResult.createSuccess(users);
  }

  @ApiOperation(value = "用户分页列表", httpMethod = "GET", produces = "application/json")
  @RequiresRoles("admin")
  @RequestMapping(value = "page")
  public ResponseResult<Page<BaseUser>> page(PageQuery pageQuery, Boolean advance, HttpServletRequest request) {
    Page<BaseUser> page = null;
    if (BooleanUtils.isTrue(advance)) {
      JqGridFilter jqGridFilter = JsonMapper.INSTANCE.fromJson(request.getParameter("filters"), JqGridFilter.class);
      page = baseUserService.getPage(new SearchRequest(jqGridFilter, pageQuery));
    } else {
      page = baseUserService.getPage(new SearchRequest(pageQuery, BaseUser.PROP_NAME, BaseUser.PROP_LOGIN_NAME, BaseUser.PROP_MOBILE));
    }
    return ResponseResult.createSuccess(page);
  }

  @ApiOperation(value = "用户分页列表", httpMethod = "GET", produces = "application/json")
  @RequiresRoles("admin")
  @RequestMapping(value = "list")
  public ResponseResult<List<BaseUser>> list(PageQuery pageQuery, HttpServletRequest request) {
    Page<BaseUser> page = baseUserService.getPage(new SearchRequest(pageQuery, BaseUser.PROP_NAME, BaseUser.PROP_LOGIN_NAME, BaseUser.PROP_MOBILE));
    return ResponseResult.createSuccess(page.getContent(), page.getTotalElements());
  }

  @ApiOperation(value = "用户获取", httpMethod = "GET", produces = "application/json")
  @RequestMapping(value = "get/{id}")
  public ResponseResult<BaseUser> get(@PathVariable("id") Long id) {
    BaseUser user = baseUserService.get(id);
    return ResponseResult.createSuccess(user);
  }

  @ApiOperation(value = "检查用户名是否存在", httpMethod = "GET")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "loginName", value = "用户名", required = true, dataType = "string", paramType = "query")
  })
  @RequestMapping(value = "checkLoginName")
  public boolean checkLoginName(Long id, String loginName) {
    return !baseUserService.existLoginName(id, loginName);
  }

}
