package com.foomei.core.dao.jpa;

import com.foomei.common.dao.JpaDao;
import com.foomei.core.entity.Config;

public interface ConfigDao extends JpaDao<Config, Long> {

	Config findByCode(String code);
}
