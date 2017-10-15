package com.foomei.core.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 消息内容
 *
 * @author walker
 */
@Data
@Entity
@Table(name = "Core_Message_Content")
@SuppressWarnings("serial")
public class MessageContent implements Serializable {

  public static final String PROP_ID = "id";
  public static final String PROP_CONTENT = "content";

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;
  private String content;//内容

}
