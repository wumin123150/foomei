package com.foomei.core.web.api;

import com.baidu.unbiz.fluentvalidator.*;
import com.baidu.unbiz.fluentvalidator.jsr303.HibernateSupportedValidator;
import com.foomei.common.dto.PageQuery;
import com.foomei.common.dto.ResponseResult;
import com.foomei.common.mapper.BeanMapper;
import com.foomei.common.mapper.JsonMapper;
import com.foomei.common.persistence.JqGridFilter;
import com.foomei.common.persistence.search.SearchRequest;
import com.foomei.core.entity.Annex;
import com.foomei.core.entity.BaseUser;
import com.foomei.core.entity.User;
import com.foomei.core.service.AnnexService;
import com.foomei.core.service.BaseUserService;
import com.foomei.core.service.UserService;
import com.foomei.core.vo.UserVo;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Validation;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Api(description = "用户接口")
@RestController
@RequestMapping(value = "/api/user")
public class UserEndpoint {

  @Autowired
  private UserService userService;
  @Autowired
  private BaseUserService baseUserService;
  @Autowired
  private AnnexService annexService;

  static {
    Map<String, String> mapFields = Maps.newHashMap();
    mapFields.put("plainPassword", "password");
    mapFields.put("roleList{id}", "roles{}");
    mapFields.put("groupList{id}", "groups{}");
    BeanMapper.registerClassMap(User.class, UserVo.class, mapFields);
  }

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

  @ApiOperation(value = "用户新增", httpMethod = "POST")
  @RequiresRoles("admin")
  @RequestMapping(value = "create", method = RequestMethod.POST)
  public ResponseResult create(@RequestBody UserVo userVo) throws IOException {
    User user = BeanMapper.map(userVo, User.class);

    ComplexResult result = validate(user);
    if (!result.isSuccess()) {
      return ResponseResult.createParamError(result);
    }

    user = userService.save(user);

    if(StringUtils.isNotEmpty(userVo.getAvatarId())) {
      Annex annex = annexService.move(userVo.getAvatarId(), User.USER_ANNEX_PATH, String.valueOf(user.getId()), User.USER_ANNEX_TYPE);
      user.setAvatar(annex.getPath());
      userService.save(user);
    }

    return ResponseResult.SUCCEED;
  }

  @ApiOperation(value = "用户修改", httpMethod = "POST")
  @RequiresRoles("admin")
  @RequestMapping(value = "update", method = RequestMethod.POST)
  public ResponseResult update(@RequestBody UserVo userVo) throws IOException {
    User user = userService.get(userVo.getId());
    user = BeanMapper.map(userVo, user, UserVo.class, User.class);
    user.setPlainPassword(null);//修改不能设置密码

    if(StringUtils.isNotEmpty(userVo.getAvatarId())) {
      Annex annex = annexService.move(userVo.getAvatarId(), User.USER_ANNEX_PATH, String.valueOf(user.getId()), User.USER_ANNEX_TYPE);
      user.setAvatar(annex.getPath());
      userService.save(user);
    }

    ComplexResult result = validate(user);
    if (!result.isSuccess()) {
      return ResponseResult.createParamError(result);
    }

    userService.save(user);
    return ResponseResult.SUCCEED;
  }

  @ApiOperation(value = "密码重置", httpMethod = "POST")
  @RequiresRoles("admin")
  @RequestMapping(value = "reset", method = RequestMethod.POST)
  public ResponseResult reset(String loginName, String plainPassword) {
    if (StringUtils.isEmpty(plainPassword)) {
      return ResponseResult.createParamError("密码不能为空");
    }

    userService.changePassword(loginName, plainPassword);
    return ResponseResult.SUCCEED;
  }

  @ApiOperation(value = "用户停用", httpMethod = "GET")
  @RequiresRoles("admin")
  @RequestMapping(value = "delete/{id}")
  public ResponseResult delete(@PathVariable("id") Long id) {
    userService.delete(id);
    return ResponseResult.SUCCEED;
  }

  @ApiOperation(value = "用户启用", httpMethod = "GET")
  @RequiresRoles("admin")
  @RequestMapping(value = "start/{id}")
  public ResponseResult start(@PathVariable("id") Long id) {
    userService.start(id);
    return ResponseResult.SUCCEED;
  }

  @ApiOperation(value = "检查用户名是否存在", httpMethod = "GET")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "loginName", value = "用户名", required = true, dataType = "string", paramType = "query")
  })
  @RequestMapping(value = "checkLoginName")
  public boolean checkLoginName(Long id, String loginName) {
    return !baseUserService.existLoginName(id, loginName);
  }

  private ComplexResult validate(User user) {
    ComplexResult result = FluentValidator.checkAll()
      .on(user, new HibernateSupportedValidator<User>().setHiberanteValidator(Validation.buildDefaultValidatorFactory().getValidator()))
      .on(user, new ValidatorHandler<User>() {
        public boolean validate(ValidatorContext context, User t) {
          if (userService.existLoginName(t.getId(), t.getLoginName())) {
            context.addErrorMsg("账号已经被使用");
            return false;
          }
          return true;
        }
      })
      .doValidate()
      .result(ResultCollectors.toComplex());
    return result;
  }

}
