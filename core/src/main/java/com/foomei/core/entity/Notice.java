package com.foomei.core.entity;

import com.foomei.common.entity.CreateRecord;
import com.foomei.common.entity.UpdateRecord;
import com.foomei.common.entity.UuidEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 通知
 *
 * @author walker
 */
@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "Core_Notice")
@SuppressWarnings("serial")
public class Notice extends UuidEntity implements CreateRecord, UpdateRecord {

  public static final String PROP_TITLE = "title";
  public static final String PROP_CONTENT = "content";
  public static final String PROP_STATUS = "status";
  public static final String PROP_CREATE_TIME = "createTime";
  public static final String PROP_CREATOR = "creator";
  public static final String PROP_UPDATE_TIME = "updateTime";
  public static final String PROP_UPDATOR = "updator";

  private String title;
  private String content;
  private Integer status;
  private Date createTime;
  private Long creator;
  private Date updateTime;
  private Long updator;

  public boolean isCreated() {
    return StringUtils.isNotEmpty(id);
  }

}
