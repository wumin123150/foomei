package com.foomei.core.web.filter;

import com.foomei.common.collection.ArrayUtil;
import com.foomei.common.net.RequestUtil;
import com.foomei.common.security.shiro.AccessToken;
import com.foomei.common.security.shiro.ShiroUser;
import com.foomei.core.service.TokenService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 自定义登录认证filter
 */
public class AccessAuthenticationFilter extends PasswordAuthenticationFilter {

  @Autowired
  private TokenService tokenService;

  public static final String DEFAULT_TOKEN_PARAM = "token";
  private String tokenParam = DEFAULT_TOKEN_PARAM;

  public boolean continueLogin(ServletRequest request, ServletResponse response) throws Exception {
    String token = getToken(request);
    if (StringUtils.isNotBlank(token)) {
      String host = getHost(request);
      try {
        Subject subject = getSubject(request, response);
        subject.login(new AccessToken(token, host));
        return true;
      } catch (AuthenticationException e) {
        e.printStackTrace();
        return false;
      }
    }

    return false;
  }

  public void renderSuccess(Subject subject, ServletRequest request, ServletResponse response, String... headers) {
    ShiroUser user = (ShiroUser) subject.getPrincipal();
    String authcToken = tokenService.apply(user.getId(), null, null);
    if(headers == null)
      headers = new String[] {String.format("AuthcToken:%s", authcToken)};
    else
      headers = ArrayUtil.concat(headers, String.format("AuthcToken:%s", authcToken));
    super.renderSuccess(subject, request, response, headers);
  }

  public String getTokenParam() {
    return tokenParam;
  }

  protected String getToken(ServletRequest request) {
    return org.apache.shiro.util.StringUtils.clean(RequestUtil.getHeader((HttpServletRequest)request, getTokenParam()));
  }

}
