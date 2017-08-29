package com.foomei.core.dao.jpa;

import com.foomei.common.dao.JpaDao;
import com.foomei.core.entity.BaseUser;

public interface BaseUserDao extends JpaDao<BaseUser, Long> {
	
	BaseUser findByLoginName(String loginName);
	
}
