package com.foomei.upms.shiro.listener;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShiroSessionListener implements SessionListener {

  private static final Logger LOGGER = LoggerFactory.getLogger(ShiroSessionListener.class);

  @Override
  public void onStart(Session session) {
    LOGGER.debug("会话创建：" + session.getId());
  }

  @Override
  public void onStop(Session session) {
    LOGGER.debug("会话停止：" + session.getId());
  }

  @Override
  public void onExpiration(Session session) {
    LOGGER.debug("会话过期：" + session.getId());
  }

}