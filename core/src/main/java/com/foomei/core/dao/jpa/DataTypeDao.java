package com.foomei.core.dao.jpa;

import com.foomei.common.dao.JpaDao;
import com.foomei.core.entity.DataType;

public interface DataTypeDao extends JpaDao<DataType, Long> {

	DataType findByCode(String code);

}
