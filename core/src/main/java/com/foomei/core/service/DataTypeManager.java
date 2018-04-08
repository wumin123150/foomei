package com.foomei.core.service;

import com.foomei.common.service.impl.MybatisServiceImpl;
import com.foomei.core.dao.mybatis.DataTypeMapper;
import com.foomei.core.entity.DataType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DataTypeManager extends MybatisServiceImpl<DataType, Long> {

  @Autowired
  private DataTypeMapper dataTypeMapper;

  public DataType getByCode(String code) {
    DataType dataType = new DataType();
    dataType.setCode(code);
    return dataTypeMapper.selectOne(dataType);
  }

  public boolean existCode(Long id, String code) {
    DataType dataType = getByCode(code);
    if (dataType == null || (id != null && id.equals(dataType.getId()))) {
      return false;
    } else {
      return true;
    }
  }

}
