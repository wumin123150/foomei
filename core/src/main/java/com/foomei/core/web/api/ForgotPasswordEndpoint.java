package com.foomei.core.web.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.foomei.common.base.annotation.LogIgnore;
import com.foomei.common.dto.ErrorCodeFactory;
import com.foomei.common.dto.ResponseResult;
import com.foomei.common.security.shiro.CaptchaToken;
import com.foomei.core.service.UserService;

@Api(description = "找回密码接口")
@RestController
public class ForgotPasswordEndpoint {

	@Autowired
	private UserService userService;

	@ApiOperation(value = "密码找回", httpMethod = "POST", produces = "application/json")
	@LogIgnore(value = "field", excludes = {"password"})
	@RequestMapping(value = "/api/retrieve", method = RequestMethod.POST)
	public ResponseResult retrieve(@ApiParam(value="用户名", required=true)String username, @ApiParam(value="密码", required=true)String password, @ApiParam(value="验证码", required=true)String captcha) {
		String phoneCode = (String) SecurityUtils.getSubject().getSession().getAttribute(CaptchaToken.CAPTCHA_SESSION_PHONE);
        String captchaCode = (String) SecurityUtils.getSubject().getSession().getAttribute(CaptchaToken.CAPTCHA_SESSION_KEY);
		if (StringUtils.isEmpty(captchaCode)) {
		    return ResponseResult.createError(ErrorCodeFactory.BAD_REQUEST, "验证码已过期");
		} else if (!captchaCode.equalsIgnoreCase(captcha)) {
		    return ResponseResult.createError(ErrorCodeFactory.BAD_REQUEST, "验证码错误");
		} else if (StringUtils.isEmpty(phoneCode) || !StringUtils.equals(username, phoneCode)) {
		    return ResponseResult.createError(ErrorCodeFactory.BAD_REQUEST, "用户名和验证码不匹配");
		}
		
		userService.changePassword(username, password);
		
		return ResponseResult.SUCCEED;
	}

}
