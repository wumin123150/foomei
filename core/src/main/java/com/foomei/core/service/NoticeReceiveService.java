package com.foomei.core.service;

import com.foomei.common.service.impl.JpaServiceImpl;
import com.foomei.core.dao.jpa.DataDictionaryDao;
import com.foomei.core.dao.jpa.NoticeReceiveDao;
import com.foomei.core.entity.*;
import com.foomei.core.web.CoreThreadContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 通知接收管理业务类.
 *
 * @author walker
 */
@Service
@Transactional(readOnly = true)
public class NoticeReceiveService extends JpaServiceImpl<NoticeReceive, String> {

  @Autowired
  private NoticeReceiveDao noticeReceiveDao;

  @Transactional(readOnly = false)
  public void read(String id) {
    NoticeReceive noticeReceive = get(id);
    noticeReceive.setStatus(NoticeReceive.STATUS_READED);
    noticeReceive.setReadTime(new Date());
    save(noticeReceive);
  }

  public List<NoticeReceive> findByNotice(String noticeId) {
    return noticeReceiveDao.findByNotice(noticeId);
  }

  public Page<NoticeReceive> getMyPage(final String searchKey, Pageable page) {
    return noticeReceiveDao.findAll(new Specification<NoticeReceive>() {
      public Predicate toPredicate(Root<NoticeReceive> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<Predicate>();

        if (StringUtils.isNotEmpty(searchKey)) {
          predicates.add(cb.like(root.get(NoticeReceive.PROP_NOTICE).get(Notice.PROP_TITLE).as(String.class), "%" + StringUtils.trimToEmpty(searchKey) + "%"));
        }

        predicates.add(cb.equal(root.get(NoticeReceive.PROP_USER).get(BaseUser.PROP_ID).as(Long.class), CoreThreadContext.getUserId()));
        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
      }
    }, page);
  }

  public long countMyUnread(final String loginName) {
    return noticeReceiveDao.count(new Specification<NoticeReceive>() {
      public Predicate toPredicate(Root<NoticeReceive> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(cb.equal(root.get(NoticeReceive.PROP_USER).get(BaseUser.PROP_LOGIN_NAME).as(String.class), loginName));
        predicates.add(cb.equal(root.get(NoticeReceive.PROP_STATUS).as(Integer.class), NoticeReceive.STATUS_UNREAD));
        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
      }
    });
  }

}
