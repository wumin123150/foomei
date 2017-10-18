package com.foomei.core.dao.jpa;

import com.foomei.common.dao.JpaDao;
import com.foomei.core.entity.Message;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageDao extends JpaDao<Message, String> {

  @Query("SELECT entity FROM Message entity WHERE entity.text.id=:textId")
  List<Message> findByText(@Param("textId") String textId);

}
