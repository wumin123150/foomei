package com.foomei.core.entity;

import com.foomei.common.entity.CreateRecord;
import com.foomei.common.entity.UuidEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * 消息内容
 *
 * @author walker
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "Core_Message_Text")
@SuppressWarnings("serial")
public class MessageText extends UuidEntity implements CreateRecord {

  public static final String PROP_CONTENT = "content";
  public static final String PROP_SENDER = "sender";
  public static final String PROP_CREATE_TIME = "createTime";
  public static final String PROP_CREATOR = "creator";

  private String content;//内容
  @ManyToOne
  private BaseUser sender;//发送人
  private Date createTime;//创建时间
  private Long creator;//创建人

  public MessageText(String content, BaseUser sender) {
    this.content = content;
    this.sender = sender;
  }

  public boolean isCreated() {
    return id != null;
  }

}
