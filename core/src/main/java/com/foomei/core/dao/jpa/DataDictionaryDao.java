package com.foomei.core.dao.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.foomei.common.dao.JpaDao;
import com.foomei.core.entity.DataDictionary;

public interface DataDictionaryDao extends JpaDao<DataDictionary, Long> {

    @Query("SELECT entity FROM DataDictionary entity WHERE entity.type.code=:typeCode AND entity.code=:code")
    DataDictionary findByTypeAndCode(@Param("typeCode")String typeCode, @Param("code")String code);
    
	@Query("SELECT entity FROM DataDictionary entity WHERE entity.type.code=:typeCode ORDER BY entity.priority ASC")
	List<DataDictionary> findByTypeCode(@Param("typeCode")String typeCode);
	
	@Query("SELECT entity FROM DataDictionary entity WHERE entity.type.code=:typeCode AND entity.level=:level ORDER BY entity.priority ASC")
	List<DataDictionary> findByTypeAndLevel(@Param("typeCode")String typeCode, @Param("level")Integer level);
	
	@Query("SELECT entity FROM DataDictionary entity WHERE entity.type.code=:typeCode AND entity.parentId=:parentId ORDER BY entity.priority ASC")
	List<DataDictionary> findChildrenByTypeAndParent(@Param("typeCode")String typeCode, @Param("parentId")Long parentId);
	
}
