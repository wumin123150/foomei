package com.foomei.core.dao.jpa;

import com.foomei.common.dao.JpaDao;
import com.foomei.core.entity.NoticeReceive;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NoticeReceiveDao extends JpaDao<NoticeReceive, String> {

  @Query("SELECT entity FROM NoticeReceive entity WHERE entity.notice.id=:noticeId")
  List<NoticeReceive> findByNotice(@Param("noticeId") String noticeId);

}
