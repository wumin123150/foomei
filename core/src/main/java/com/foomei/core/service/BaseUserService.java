package com.foomei.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foomei.common.service.impl.JpaServiceImpl;
import com.foomei.core.dao.jpa.BaseUserDao;
import com.foomei.core.entity.BaseUser;

/**
 * 用户管理业务类.
 *
 * @author walker
 */
@Service
@Transactional(readOnly = true)
public class BaseUserService extends JpaServiceImpl<BaseUser, Long> {

  @Autowired
  private BaseUserDao baseUserDao;

  public BaseUser getByLoginName(String loginName) {
    return baseUserDao.findByLoginName(loginName);
  }

  public boolean existLoginName(Long id, String loginName) {
    BaseUser user = getByLoginName(loginName);
    if (user == null || (id != null && id.equals(user.getId()))) {
      return false;
    } else {
      return true;
    }
  }

}
