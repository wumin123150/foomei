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
 * 通知接收
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
  private Notice notice;
  @ManyToOne
  @JoinColumn(name = "user_id")
  private BaseUser user;
  private Integer status;
  private Date readTime;

  public NoticeReceive(Notice notice, BaseUser user) {
    this.notice = notice;
    this.user = user;
    this.status = STATUS_UNREAD;
  }



}
