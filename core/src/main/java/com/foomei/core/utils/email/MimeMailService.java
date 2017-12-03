package com.foomei.core.utils.email;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.multipart.MultipartFile;

import com.foomei.common.text.FreeMarkerUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * MIME邮件服务类.
 * <p>
 * 演示由Freemarker引擎生成的的html格式邮件, 并带有附件.
 *
 * @author walker
 */
public class MimeMailService {

  private static final String DEFAULT_ENCODING = "utf-8";

  private static final Logger LOGGER = LoggerFactory.getLogger(MimeMailService.class);

  private JavaMailSender mailSender;

  private Configuration configuration;

  private String from;

  /**
   * 发送MIME格式邮件.
   */
  public void sendMail(String to, String subject, String templateName, Map<String, ?> model) {
    sendMail(to, subject, templateName, model, null);
  }

  /**
   * 发送MIME格式邮件.
   */
  public void sendMail(String to, String subject, String templateName, Map<String, ?> model, List<MultipartFile> attachments) {
    sendMail(to, null, null, subject, templateName, model, attachments);
  }

  /**
   * 发送MIME格式邮件.
   */
  public void sendMail(String to, String cc, String bcc, String subject, String templateName, Map<String, ?> model, List<MultipartFile> attachments) {
    try {
      MimeMessage msg = mailSender.createMimeMessage();
      MimeMessageHelper helper = new MimeMessageHelper(msg, true, DEFAULT_ENCODING);

      String content = generateContent(templateName, model);

      helper.setFrom(from);
      helper.setTo(to);
      if (StringUtils.isNotEmpty(cc))
        helper.setCc(cc);
      if (StringUtils.isNotEmpty(bcc))
        helper.setBcc(bcc);
      helper.setSubject(subject);
      helper.setText(content, true);

      if (attachments != null) {
        for (MultipartFile attachment : attachments) {
          helper.addAttachment(attachment.getOriginalFilename(), new ByteArrayResource(attachment.getBytes()));
        }
      }

      mailSender.send(msg);
      LOGGER.info("HTML版邮件已发送至", to);
    } catch (MessagingException e) {
      LOGGER.error("构造邮件失败", e);
    } catch (Exception e) {
      LOGGER.error("发送邮件失败", e);
    }
  }

  /**
   * 使用Freemarker生成html格式内容.
   */
  private String generateContent(String templateName, Map<String, ?> model) throws MessagingException {
    try {
      Template template = configuration.getTemplate(templateName, DEFAULT_ENCODING);
      return FreeMarkerUtil.renderTemplate(template, model);
    } catch (IOException e) {
      LOGGER.error("生成邮件内容失败, FreeMarker模板不存在", e);
      throw new MessagingException("FreeMarker模板不存在", e);
    } catch (RuntimeException e) {
      LOGGER.error("生成邮件内容失败, FreeMarker处理失败", e);
      throw new MessagingException("FreeMarker处理失败", e);
    }
  }

  /**
   * Spring的MailSender.
   */
  public void setMailSender(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  /**
   * 注入Freemarker引擎配置.
   */
  public void setFreemarkerConfiguration(Configuration freemarkerConfiguration) {
    configuration = freemarkerConfiguration;
  }

  /**
   * 注入发送者.
   */
  public void setFrom(String from) {
    this.from = from;
  }

}
