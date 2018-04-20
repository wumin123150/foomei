package com.foomei.core.dao.jpa;

import com.foomei.common.dao.JpaDao;
import com.foomei.core.entity.User;

public interface UserDao extends JpaDao<User, Long> {

	User findByOpenId(String openId);
	
}
