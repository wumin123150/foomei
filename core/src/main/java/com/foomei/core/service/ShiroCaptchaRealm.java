package com.foomei.core.service;

import com.foomei.common.mapper.BeanMapper;
import com.foomei.common.security.shiro.*;
import com.foomei.core.entity.Role;
import com.foomei.core.entity.User;
import com.foomei.core.entity.UserGroup;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Set;

public class ShiroCaptchaRealm extends AuthorizingRealm {

  @Autowired
  private UserService userService;

  public ShiroCaptchaRealm(CacheManager cacheManager) {
    super(cacheManager);
  }


  /**
   * 认证回调函数,登录时调用.
   */
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
    String username = ((CaptchaToken) authcToken).getUsername();
    String captcha = ((CaptchaToken) authcToken).getCaptcha();
    if (username == null) {
      throw new AccountException("Null username are not allowed by this realm.");
    }

    String usernameCode = (String) SecurityUtils.getSubject().getSession().getAttribute(CaptchaToken.CAPTCHA_SESSION_PHONE);
    String captchaCode = (String) SecurityUtils.getSubject().getSession().getAttribute(CaptchaToken.CAPTCHA_SESSION_KEY);
    if (StringUtils.isEmpty(captchaCode)) {
      throw new CaptchaException("动态密码已过期.");
    } else if (!StringUtils.equalsIgnoreCase(captcha, captchaCode)) {
      throw new CaptchaException("动态密码错误.");
    } else if (StringUtils.isEmpty(usernameCode) || !StringUtils.equals(username, usernameCode)) {
      throw new CaptchaException("手机和动态密码不匹配.");
    }

    User user = userService.getByLoginName(username);

    if (user != null) {
      if (!user.isEnabled()) {
        if (user.isAccountInactived()) {
          throw new InactiveAccountException();
        }
        if (user.isAccountExpired()) {
          throw new ExpiredCredentialsException();
        }
        if (user.isAccountLocked()) {
          throw new LockedAccountException();
        }
        if (user.isAccountTerminated()) {
          throw new DisabledAccountException();
        }
      }

      return new SimpleAuthenticationInfo(BeanMapper.map(user, ShiroUser.class), null, getName());
    } else {
      throw new UnknownAccountException("No account found");
    }
  }

  /**
   * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
   */
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
    User user = userService.getByLoginName(shiroUser.getLoginName());

    Set<Role> roles = Sets.newTreeSet(new RoleComparator());
    for (UserGroup group : user.getGroupList()) {
      roles.addAll(group.getRoleList());
    }
    roles.addAll(user.getRoleList());

    SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
    for (Role role : roles) {
      // 基于Role的权限信息
      info.addRole(role.getCode());
      // 基于Permission的权限信息
      info.addStringPermissions(role.getPermissions());
    }
    return info;
  }

  /**
   * 设定Password校验的Hash算法与迭代次数.
   */
  @PostConstruct
  public void initCredentialsMatcher() {
    SkipCredentialsMatcher matcher = new SkipCredentialsMatcher();
    setCredentialsMatcher(matcher);
  }

  @Override
  public void onLogout(PrincipalCollection principals) {
    super.clearCachedAuthorizationInfo(principals);

    ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
    SimplePrincipalCollection collection = new SimplePrincipalCollection();
    collection.add(shiroUser.getLoginName(), super.getName());
    super.clearCachedAuthenticationInfo(collection);
  }

  @PostConstruct
  public void initAuthenticationTokenClass() {
    setAuthenticationTokenClass(CaptchaToken.class);
  }

  public static class RoleComparator implements Comparator<Role>, Serializable {
    public int compare(Role role1, Role role2) {
      return role1.getCode().compareTo(role2.getCode());
    }
  }
}
