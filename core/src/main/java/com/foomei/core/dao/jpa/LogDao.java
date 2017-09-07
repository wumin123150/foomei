package com.foomei.core.dao.jpa;

import java.util.Date;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.foomei.common.dao.JpaDao;
import com.foomei.core.entity.Log;

public interface LogDao extends JpaDao<Log, String> {

  @Modifying
  @Query("DELETE FROM Log entity WHERE entity.logTime<?1")
  void clear(Date deadline);

}
