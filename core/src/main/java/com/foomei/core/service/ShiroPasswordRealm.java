package com.foomei.core.service;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Set;

import javax.annotation.PostConstruct;

import com.foomei.common.security.shiro.ShiroUser;
import com.foomei.common.mapper.BeanMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import com.foomei.common.security.shiro.InactiveAccountException;
import com.foomei.common.text.EncodeUtil;
import com.foomei.core.entity.Role;
import com.foomei.core.entity.User;
import com.foomei.core.entity.UserGroup;
import com.google.common.collect.Sets;

public class ShiroPasswordRealm extends AuthorizingRealm {

  protected UserService userService;

  /**
   * 认证回调函数,登录时调用.
   */
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
    String username = (String) authcToken.getPrincipal();
    if (username == null) {
      throw new AccountException("Null username are not allowed by this realm.");
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

      if (StringUtils.isNotEmpty(user.getSalt())) {
        byte[] salt = EncodeUtil.decodeHex(StringUtils.upperCase(user.getSalt()));
        return new SimpleAuthenticationInfo(BeanMapper.map(user, ShiroUser.class), user.getPassword(), ByteSource.Util.bytes(salt), getName());
      } else {
        return new SimpleAuthenticationInfo(BeanMapper.map(user, ShiroUser.class), user.getPassword(), getName());
      }
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
    HashedCredentialsMatcher matcher = new HashedCredentialsMatcher(UserService.HASH_ALGORITHM);
    matcher.setHashIterations(UserService.HASH_INTERATIONS);

    setCredentialsMatcher(matcher);
  }

  @PostConstruct
  public void initAuthenticationTokenClass() {
    setAuthenticationTokenClass(UsernamePasswordToken.class);
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public static class RoleComparator implements Comparator<Role>, Serializable {
    public int compare(Role role1, Role role2) {
      return role1.getCode().compareTo(role2.getCode());
    }
  }
}
