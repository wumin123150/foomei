package com.foomei.core.service;

import com.foomei.common.service.impl.JpaServiceImpl;
import com.foomei.core.entity.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 消息管理业务类.
 *
 * @author walker
 */
@Service
@Transactional(readOnly = true)
public class MessageService extends JpaServiceImpl<Message, String> {

}
