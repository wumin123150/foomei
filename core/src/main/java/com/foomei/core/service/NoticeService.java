package com.foomei.core.service;

import com.foomei.common.collection.CollectionExtractor;
import com.foomei.common.collection.ListUtil;
import com.foomei.common.service.impl.JpaServiceImpl;
import com.foomei.core.entity.BaseUser;
import com.foomei.core.entity.Notice;
import com.foomei.core.entity.NoticeReceive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 通知管理业务类.
 *
 * @author walker
 */
@Service
@Transactional(readOnly = true)
public class NoticeService extends JpaServiceImpl<Notice, String> {

  @Autowired
  private NoticeReceiveService noticeReceiveService;

  @Transactional(readOnly = false)
  public Notice save(Notice notice, List<Long> userIds) {
    notice = save(notice);

    List<NoticeReceive> noticeReceives = noticeReceiveService.findByNotice(notice.getId());
    List<Long> oldUserIds = ListUtil.newArrayList();
    for(NoticeReceive noticeReceive : noticeReceives) {
      oldUserIds.add(noticeReceive.getUser().getId());
    }

    List<Long> createUserIds = ListUtil.difference(userIds, oldUserIds);
    List<Long> deleteUserIds = ListUtil.difference(oldUserIds, userIds);

    for(Long userId : createUserIds) {
      noticeReceiveService.save(new NoticeReceive(notice, new BaseUser(userId)));
    }

    for(NoticeReceive noticeReceive : noticeReceives) {
      if(deleteUserIds.contains(noticeReceive.getUser().getId())) {
        noticeReceiveService.delete(noticeReceive);
      }
    }

    return notice;
  }

}
