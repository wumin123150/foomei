package com.foomei.core.utils.email;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

/**
 * 纯文本邮件服务类.
 *
 * @author walker
 */
public class SimpleMailService {
  private static Logger logger = LoggerFactory.getLogger(SimpleMailService.class);

  private JavaMailSender mailSender;

  private String from;

  /**
   * 发送纯文本邮件.
   */
  public void sendMail(String to, String subject, String content) {
    sendMail(to, null, null, subject, content);
  }

  /**
   * 发送纯文本邮件.
   */
  public void sendMail(String to, String cc, String subject, String content) {
    sendMail(to, cc, null, subject, content);
  }

  /**
   * 发送纯文本邮件.
   */
  public void sendMail(String to, String cc, String bcc, String subject, String content) {
    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setFrom(from);
    msg.setTo(to);
    if (StringUtils.isNotEmpty(cc))
      msg.setCc(cc);
    if (StringUtils.isNotEmpty(bcc))
      msg.setBcc(bcc);
    msg.setSubject(subject);
    msg.setText(content);

    try {
      mailSender.send(msg);
      if (logger.isInfoEnabled()) {
        logger.info("纯文本邮件已发送至{}", StringUtils.join(msg.getTo(), ","));
      }
    } catch (Exception e) {
      logger.error("发送邮件失败", e);
    }
  }

  /**
   * Spring的MailSender.
   */
  public void setMailSender(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  /**
   * 注入发送者.
   */
  public void setFrom(String from) {
    this.from = from;
  }

}
