package com.foomei.core.entity;

import com.foomei.common.entity.UuidEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * 用户通知
 *
 * @author walker
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "Core_Notice_Receive")
@SuppressWarnings("serial")
public class NoticeReceive extends UuidEntity {

  public static final String PROP_NOTICE = "notice";
  public static final String PROP_USER = "user";
  public static final String PROP_STATUS = "status";
  public static final String PROP_READ_TIME = "readTime";

  public static final Integer STATUS_UNREAD = 0;
  public static final Integer STATUS_READED = 1;

  @ManyToOne
  @JoinColumn(name = "notice_id")
  private Notice notice;//通知
  @ManyToOne
  @JoinColumn(name = "user_id")
  private BaseUser user;//用户
  private Integer sendStatus;//发送状态(0:发送中,1:已发送,2:发送失败)
  private Integer readStatus;//阅读状态(0:未读,1:已读)
  private Date readTime;//阅读时间

  public NoticeReceive(Notice notice, BaseUser user) {
    this.notice = notice;
    this.user = user;
    this.readStatus = STATUS_UNREAD;
  }



}
