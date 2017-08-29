package com.foomei.core.web.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import com.foomei.common.net.CookieUtil;
import com.foomei.common.security.shiro.AccessToken;
import com.foomei.core.dto.ShiroUser;
import com.foomei.core.service.TokenService;

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
        String authToken = tokenService.apply(user.getId(), null, null);
        CookieUtil.cancelCookie((HttpServletRequest)request, (HttpServletResponse)response, "token", null);
        CookieUtil.addCookie((HttpServletRequest)request, (HttpServletResponse)response, "token", authToken, -1, null);
        super.renderSuccess(subject, request, response, headers);
    }

    public String getTokenParam() {
        return tokenParam;
    }

    protected String getToken(ServletRequest request) {
        return org.apache.shiro.util.StringUtils.clean(((HttpServletRequest)request).getHeader(getTokenParam()));
    }

}
