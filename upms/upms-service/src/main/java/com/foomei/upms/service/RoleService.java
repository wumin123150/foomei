package com.foomei.upms.service;

import com.foomei.common.service.impl.JpaServiceImpl;
import com.foomei.upms.dao.jpa.RoleDao;
import com.foomei.upms.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 角色管理业务类.
 *
 * @author walker
 */
@Service
@Transactional(readOnly = true)
public class RoleService extends JpaServiceImpl<Role, Long> {

  @Autowired
  private RoleDao roleDao;

  public Role getByCode(String code) {
    return roleDao.findByCode(code);
  }

  public boolean existCode(Long id, String code) {
    Role role = getByCode(code);
    if (role == null || (id != null && id.equals(role.getId()))) {
      return false;
    } else {
      return true;
    }
  }

}
