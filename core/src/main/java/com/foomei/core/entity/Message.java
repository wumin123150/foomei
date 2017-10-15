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
  private BaseUser sender;//发件人
  private Integer senderStatus;//发件人状态(0:草稿箱,1:发件箱,3:收藏箱,4:垃圾箱,5:已删除)
  private Date sendTime;//发送时间
  @ManyToOne
  private BaseUser receiver;//收件人
  private Integer receiverStatus;//收件人状态(2:收件箱,3:收藏箱,4:垃圾箱,5:已删除)
  private String title;//标题
  private Boolean isRead;//是否已读
  private Boolean isReply;//是否已回复
  private String parentId;//父ID

}
