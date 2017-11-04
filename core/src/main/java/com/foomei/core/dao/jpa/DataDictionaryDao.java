package com.foomei.core.dao.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.foomei.common.dao.JpaDao;
import com.foomei.core.entity.DataDictionary;

public interface DataDictionaryDao extends JpaDao<DataDictionary, Long> {

  @Query("SELECT entity FROM DataDictionary entity WHERE entity.type.code=:typeCode AND entity.code=:code")
  DataDictionary findByTypeAndCode(@Param("typeCode") String typeCode, @Param("code") String code);

  @Query("SELECT entity FROM DataDictionary entity WHERE entity.type.id=:typeId ORDER BY entity.priority ASC")
  List<DataDictionary> findByType(@Param("typeId") Long typeId);

  @Query("SELECT entity FROM DataDictionary entity WHERE entity.type.code=:typeCode ORDER BY entity.priority ASC")
  List<DataDictionary> findByTypeCode(@Param("typeCode") String typeCode);

  @Query("SELECT entity FROM DataDictionary entity WHERE entity.type.id=:typeId AND entity.grade=:grade ORDER BY entity.priority ASC")
  List<DataDictionary> findByTypeAndGrade(@Param("typeId") Long typeId, @Param("grade") Integer grade);

  @Query("SELECT entity FROM DataDictionary entity WHERE entity.type.id=:typeId AND entity.parentId=:parentId ORDER BY entity.priority ASC")
  List<DataDictionary> findChildrenByTypeAndParent(@Param("typeId") Long typeId, @Param("parentId") Long parentId);

  @Query("SELECT entity FROM DataDictionary entity WHERE entity.type.code=:typeCode AND entity.parentId=:parentId ORDER BY entity.priority ASC")
  List<DataDictionary> findChildrenByTypeCodeAndParent(@Param("typeCode") String typeCode, @Param("parentId") Long parentId);

}
