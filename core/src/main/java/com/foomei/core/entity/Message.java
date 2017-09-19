package com.foomei.core.entity;

import com.foomei.common.entity.UuidEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * 消息
 *
 * @author walker
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "Core_Message")
@SuppressWarnings("serial")
public class Message extends UuidEntity {

  public static final String PROP_SENDER = "sender";
  public static final String PROP_SENDER_STATUS = "senderStatus";
  public static final String PROP_SEND_TIME = "sendTime";
  public static final String PROP_RECEIVER = "receiver";
  public static final String PROP_RECEIVER_STATUS = "receiverStatus";
  public static final String PROP_TITLE = "title";
  public static final String PROP_IS_READ = "isRead";
  public static final String PROP_IS_REPLY = "isReply";
  public static final String PROP_PARENT_ID = "parentId";

  @ManyToOne
  private BaseUser sender;
  private Integer senderStatus;
  private Date sendTime;
  @ManyToOne
  private BaseUser receiver;
  private Integer receiverStatus;
  private String title;
  private Boolean isRead;
  private Boolean isReply;
  private String parentId;

}
