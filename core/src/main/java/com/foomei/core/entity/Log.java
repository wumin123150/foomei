package com.foomei.core.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.foomei.common.entity.UuidEntity;

/**
 * 日志
 *
 * @author walker
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "Core_Log")
@SuppressWarnings("serial")
public class Log extends UuidEntity {

  public static final String PROP_DESCRIPTION = "description";
  public static final String PROP_USERNAME = "username";
  public static final String PROP_LOG_TIME = "logTime";
  public static final String PROP_SPEND_TIME = "spendTime";
  public static final String PROP_IP = "ip";
  public static final String PROP_URL = "url";
  public static final String PROP_METHOD = "method";
  public static final String PROP_USER_AGENT = "userAgent";
  public static final String PROP_PARAMETER = "parameter";
  public static final String PROP_RESULT = "result";

  private String description;
  private String username;
  private Date logTime;
  private Integer spendTime;
  private String ip;
  private String url;
  private String method;
  private String userAgent;
  private String parameter;
  private String result;

}
