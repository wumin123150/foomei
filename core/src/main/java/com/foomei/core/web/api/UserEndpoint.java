package com.foomei.core.web.api;

import com.foomei.common.dto.ErrorCodeFactory;
import com.foomei.common.dto.PageQuery;
import com.foomei.common.dto.ResponseResult;
import com.foomei.common.mapper.JsonMapper;
import com.foomei.common.persistence.JqGridFilter;
import com.foomei.common.persistence.SearchFilter;
import com.foomei.core.dto.UserDto;
import com.foomei.core.entity.BaseUser;
import com.foomei.core.entity.Token;
import com.foomei.core.service.BaseUserService;
import com.foomei.core.service.TokenService;
import com.foomei.core.web.CoreThreadContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Api(description = "用户接口")
@RestController
@RequestMapping(value = "/api/user")
public class UserEndpoint {

  @Autowired
  private BaseUserService baseUserService;
  @Autowired
  private TokenService tokenService;

  @ApiOperation(value = "用户智能搜索", notes = "按名称和手机查询", httpMethod = "GET", produces = "application/json")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "q", value = "关键词, searchKey作废", required = true, dataType = "string", paramType = "query")
  })
  @RequestMapping(value = "search")
  public ResponseResult<Page<BaseUser>> search(PageQuery pageQuery, @RequestParam("q") String searchKey) {
    SearchFilter searchFilter = new SearchFilter().or()
      .addLike(BaseUser.PROP_NAME, searchKey)
      .addLike(BaseUser.PROP_LOGIN_NAME, searchKey)
      .addLike(BaseUser.PROP_MOBILE, searchKey);
    Page<BaseUser> users = baseUserService.getPage(searchFilter, pageQuery.buildPageRequest(BaseUser.PROP_NAME, "desc"));
    return ResponseResult.createSuccess(users);
  }

  @ApiOperation(value = "用户分页列表", httpMethod = "GET", produces = "application/json")
  @RequiresRoles("admin")
  @RequestMapping(value = "page")
  public ResponseResult<Page<BaseUser>> page(PageQuery pageQuery, HttpServletRequest request) {
    Page<BaseUser> page = null;
    if (pageQuery.getAdvance()) {
      JqGridFilter jqGridFilter = JsonMapper.nonDefaultMapper().fromJson(request.getParameter("filters"), JqGridFilter.class);
      page = baseUserService.getPage(jqGridFilter, pageQuery.buildPageRequest());
    } else {
      SearchFilter searchFilter = new SearchFilter().or()
        .addLike(BaseUser.PROP_NAME, pageQuery.getSearchKey())
        .addLike(BaseUser.PROP_LOGIN_NAME, pageQuery.getSearchKey())
        .addLike(BaseUser.PROP_MOBILE, pageQuery.getSearchKey());
      page = baseUserService.getPage(searchFilter, pageQuery.buildPageRequest());
    }
    return ResponseResult.createSuccess(page);
  }

  @ApiOperation(value = "用户获取", httpMethod = "GET", produces = "application/json")
  @RequestMapping(value = "get/{id}")
  public ResponseResult<BaseUser> get(@PathVariable("id") Long id) {
    BaseUser user = baseUserService.get(id);
    return ResponseResult.createSuccess(user);
  }

  @ApiOperation(value = "修改用户信息", httpMethod = "POST", produces = "application/json")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "name", value = "姓名", required = true, dataType = "string", paramType = "form"),
    @ApiImplicitParam(name = "mobile", value = "手机", dataType = "string", paramType = "form"),
    @ApiImplicitParam(name = "email", value = "电子邮件", dataType = "string", paramType = "form")
  })
  @RequestMapping(value = "change")
  public ResponseResult<UserDto> change(String name, String mobile, String email) {
    BaseUser user = baseUserService.get(CoreThreadContext.getUserId());
    user.setName(name);
    user.setMobile(mobile);
    user.setEmail(email);
    user = baseUserService.save(user);
    return ResponseResult.createSuccess(user, UserDto.class);
  }

  @ApiOperation(value = "检查token是否有效", httpMethod = "POST", produces = "application/json")
  @RequestMapping(value = "/validate/{token}")
  public ResponseResult validateToken(@PathVariable("token") String id, HttpServletRequest request) {
    Token token = tokenService.get(id);
    if (token != null && token.isEnabled() && token.getUser().isEnabled()) {
      return ResponseResult.SUCCEED;
    }

    return ResponseResult.createError(ErrorCodeFactory.BAD_REQUEST, "由于您长时间未登录 请重新登录。");
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
