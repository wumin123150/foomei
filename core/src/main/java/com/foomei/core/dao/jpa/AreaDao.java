package com.foomei.core.dao.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.foomei.common.dao.JpaDao;
import com.foomei.core.entity.Area;

public interface AreaDao extends JpaDao<Area, String> {

  Area findByCode(String code);

  @Query("SELECT entity FROM Area entity WHERE entity.level=1 ORDER BY entity.code ASC")
  List<Area> findTop();

  @Query("SELECT entity FROM Area entity WHERE entity.parentId=:parentId ORDER BY entity.code ASC")
  List<Area> findChildrenByParent(@Param("parentId") String parentId);

  @Query("SELECT entity FROM Area entity WHERE entity.level > 1 AND entity.type < 6 AND entity.rootId=:rootId ORDER BY entity.code ASC")
  List<Area> findTownByRoot(@Param("rootId") String rootId);

}
