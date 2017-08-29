package com.foomei.core.dao.jpa;

import com.foomei.common.dao.JpaDao;
import com.foomei.core.entity.Annex;

public interface AnnexDao extends JpaDao<Annex, String> {

	Annex findByPath(String path);
	
}
