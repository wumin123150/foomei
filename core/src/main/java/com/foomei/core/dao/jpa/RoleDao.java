package com.foomei.core.dao.jpa;

import com.foomei.common.dao.JpaDao;
import com.foomei.core.entity.Role;

public interface RoleDao extends JpaDao<Role, Long> {

	Role findByCode(String code);

}
