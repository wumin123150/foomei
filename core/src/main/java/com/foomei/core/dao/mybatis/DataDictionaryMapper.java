package com.foomei.core.dao.mybatis;

import com.foomei.common.dao.MybatisDao;
import com.foomei.core.entity.DataDictionary;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DataDictionaryMapper extends MybatisDao<DataDictionary> {

  DataDictionary findByTypeAndCode(@Param("typeCode") String typeCode, @Param("code") String code);

  List<DataDictionary> findByType(@Param("typeId") Long typeId);

  List<DataDictionary> findByTypeCode(@Param("typeCode") String typeCode);

  List<DataDictionary> findByTypeAndGrade(@Param("typeId") Long typeId, @Param("grade") Integer grade);

  List<DataDictionary> findChildrenByParent(@Param("parentId") Long parentId);

}
