package com.foomei.upms.web.api;

import com.foomei.common.dto.ErrorCodeFactory;
import com.foomei.common.dto.ResponseResult;
import com.foomei.upms.entity.Token;
import com.foomei.upms.service.TokenService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@RequestMapping(value = "/api/token")
public class TokenEndpoint {

  @Autowired
  private TokenService tokenService;

  @ApiOperation(value = "检查token是否有效", httpMethod = "GET", produces = "application/json")
  @RequestMapping(value = "/validate/{token}")
  public ResponseResult validateToken(@PathVariable("token") String id, HttpServletRequest request) {
    Token token = tokenService.get(id);
    if (token != null && token.isEnabled() && token.getUser().isEnabled()) {
      return ResponseResult.SUCCEED;
    }

    return ResponseResult.createError(ErrorCodeFactory.BAD_REQUEST, "由于您长时间未登录 请重新登录。");
  }

}
