package com.foomei.core.service;

import com.foomei.common.service.impl.JpaServiceImpl;
import com.foomei.core.dao.jpa.MessageTextDao;
import com.foomei.core.entity.BaseUser;
import com.foomei.core.entity.MessageText;
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
import java.util.List;

/**
 * 消息内容管理业务类.
 *
 * @author walker
 */
@Service
@Transactional(readOnly = true)
public class MessageTextService extends JpaServiceImpl<MessageText, String> {

  @Autowired
  private MessageTextDao messageTextDao;

  public Page<MessageText> getMyPage(final String searchKey, Pageable page) {
    return messageTextDao.findAll(new Specification<MessageText>() {
      public Predicate toPredicate(Root<MessageText> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<Predicate>();

        if (StringUtils.isNotEmpty(searchKey)) {
          predicates.add(cb.like(root.get(MessageText.PROP_CONTENT).as(String.class), "%" + StringUtils.trimToEmpty(searchKey) + "%"));
        }

        predicates.add(cb.equal(root.get(MessageText.PROP_SENDER).get(BaseUser.PROP_ID).as(Long.class), CoreThreadContext.getUserId()));
        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
      }
    }, page);
  }

}
