package com.foomei.core.service;

import com.foomei.common.service.impl.JpaServiceImpl;
import com.foomei.common.web.ThreadContext;
import com.foomei.core.dao.jpa.MessageTextDao;
import com.foomei.core.entity.MessageText;
import com.foomei.core.entity.QMessageText;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息内容管理业务类.
 *
 * @author walker
 */
@Service
public class MessageTextService extends JpaServiceImpl<MessageText, String> {

  @Autowired
  private MessageTextDao messageTextDao;

  public Page<MessageText> getMyPage(final String searchKey, Pageable page) {
    QMessageText qMessageText = QMessageText.messageText;
    List<BooleanExpression> expressions = new ArrayList<>();

    expressions.add(qMessageText.sender.id.eq(ThreadContext.getUserId()));

    if (StringUtils.isNotEmpty(searchKey)) {
      expressions.add(qMessageText.content.like(StringUtils.trimToEmpty(searchKey)));
    }

    return messageTextDao.findAll(Expressions.allOf(expressions.toArray(new BooleanExpression[expressions.size()])), page);
  }

}
