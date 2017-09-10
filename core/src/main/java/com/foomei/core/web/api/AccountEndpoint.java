package com.foomei.core.web.api;

import com.foomei.common.base.annotation.LogIgnore;
import com.foomei.common.dto.ErrorCodeFactory;
import com.foomei.common.dto.ResponseResult;
import com.foomei.core.dto.UserDto;
import com.foomei.core.entity.Annex;
import com.foomei.core.entity.BaseUser;
import com.foomei.core.entity.User;
import com.foomei.core.service.AnnexService;
import com.foomei.core.service.UserService;
import com.foomei.core.web.CoreThreadContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Api(description = "账户接口")
@RestController
@RequestMapping(value = "/api/account")
public class AccountEndpoint {

  @Autowired
  private UserService userService;
  @Autowired
  private AnnexService annexService;

  @ApiOperation(value = "保存头像", notes = "图片转成base64编码", httpMethod = "POST", produces = "application/json")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "avatar", value = "base64编码的图片", required = true, dataType = "string", paramType = "query")
  })
  @RequestMapping(value = "saveAvatar", method = RequestMethod.POST)
  public ResponseResult saveAvatar(String avatar) throws IOException {
    Long id = CoreThreadContext.getUserId();
    byte[] data = Base64.decodeBase64(avatar.getBytes("UTF-8"));
    Annex annex = annexService.save(data, "avatar.jpg", User.USER_ANNEX_PATH, String.valueOf(id), User.USER_ANNEX_TYPE);

    User user = userService.get(id);
    user.setAvatar(annex.getPath());
    userService.save(user);
    return ResponseResult.createSuccess(annex.getPath());
  }

  @ApiOperation(value = "密码修改", httpMethod = "POST", produces = "application/json")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "password", value = "原密码", required = true, dataType = "string", paramType = "form"),
    @ApiImplicitParam(name = "plainPassword", value = "新密码", required = true, dataType = "string", paramType = "form")
  })
  @LogIgnore(value = "field", excludes = {"password", "plainPassword"})
  @RequestMapping(value = "/changePwd", method = RequestMethod.POST)
  public ResponseResult changePassword(String password, String plainPassword) {
    Long id = CoreThreadContext.getUserId();
    if (userService.checkPassword(id, password)) {
      userService.changePassword(id, plainPassword);
      return ResponseResult.SUCCEED;
    } else {
      return ResponseResult.createError(ErrorCodeFactory.BAD_REQUEST, "原密码错误");
    }
  }

  @ApiOperation(value = "修改用户信息", httpMethod = "POST", produces = "application/json")
  @ApiImplicitParams({
    @ApiImplicitParam(name = "name", value = "姓名", required = true, dataType = "string", paramType = "form"),
    @ApiImplicitParam(name = "mobile", value = "手机", dataType = "string", paramType = "form"),
    @ApiImplicitParam(name = "email", value = "电子邮件", dataType = "string", paramType = "form")
  })
  @RequestMapping(value = "change", method = RequestMethod.POST)
  public ResponseResult<UserDto> change(String name, String mobile, String email) {
    Long id = CoreThreadContext.getUserId();
    User user = userService.get(id);
    user.setName(name);
    user.setMobile(mobile);
    user.setEmail(email);
    user = userService.save(user);
    return ResponseResult.SUCCEED;
  }

}
