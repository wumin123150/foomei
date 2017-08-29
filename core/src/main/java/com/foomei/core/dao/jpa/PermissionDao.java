package com.foomei.core.dao.jpa;

import com.foomei.common.dao.JpaDao;
import com.foomei.core.entity.Permission;

public interface PermissionDao extends JpaDao<Permission, Long> {

	Permission findByCode(String code);
}
