package com.foomei.upms.service;

import com.foomei.common.collection.ListUtil;
import com.foomei.common.service.impl.JpaServiceImpl;
import com.foomei.common.web.ThreadContext;
import com.foomei.upms.dao.jpa.MessageDao;
import com.foomei.upms.entity.BaseUser;
import com.foomei.upms.entity.Message;
import com.foomei.upms.entity.MessageText;
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
 * 消息管理业务类.
 *
 * @author walker
 */
@Service
@Transactional(readOnly = true)
public class MessageService extends JpaServiceImpl<Message, String> {

//  @Autowired
//  private SimpMessagingTemplate simpMessagingTemplate;
  @Autowired
  private MessageTextService messageTextService;
  @Autowired
  private MessageDao messageDao;

  @Transactional(readOnly = false)
  public List<Message> save(String content, Long sender, List<Long> receivers) {
    MessageText text = messageTextService.save(new MessageText(content, sender != null ? new BaseUser(sender) : null));

    List<Message> messages = ListUtil.newArrayList();
    for(Long receiver : receivers) {
      Message message = save(new Message(text, new BaseUser(receiver)));
      messages.add(message);
//      simpMessagingTemplate.convertAndSendToUser(String.valueOf(message.getReceiver().getId()), "/message", message);
    }

    return messages;
  }

  @Transactional(readOnly = false)
  public void read(String id) {
    Message message = get(id);
    message.setReadStatus(Message.READ_STATUS_READED);
    message.setReadTime(new Date());
    save(message);
  }

  public List<Message> findByText(String textId) {
    return messageDao.findByText(textId);
  }

  public Page<Message> getMyPage(final String searchKey, final Integer readStatus, Pageable page) {
    return messageDao.findAll(new Specification<Message>() {
      public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<Predicate>();

        if (StringUtils.isNotEmpty(searchKey)) {
          predicates.add(cb.like(root.get(Message.PROP_TEXT).get(MessageText.PROP_CONTENT).as(String.class), "%" + StringUtils.trimToEmpty(searchKey) + "%"));
        }

        if (readStatus != null) {
          predicates.add(cb.equal(root.get(Message.PROP_READ_STATUS).as(Integer.class), readStatus));
        }

        predicates.add(cb.equal(root.get(Message.PROP_RECEIVER).get(BaseUser.PROP_ID).as(Long.class), ThreadContext.getUserId()));
        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
      }
    }, page);
  }

  public long countMyUnread(final String loginName) {
    return messageDao.count(new Specification<Message>() {
      public Predicate toPredicate(Root<Message> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(cb.equal(root.get(Message.PROP_RECEIVER).get(BaseUser.PROP_LOGIN_NAME).as(String.class), loginName));
        predicates.add(cb.equal(root.get(Message.PROP_READ_STATUS).as(Integer.class), Message.READ_STATUS_UNREAD));
        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
      }
    });
  }

}
