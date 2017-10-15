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

  private String title;//标题
  private String content;//内容
  private Integer status;//状态(0:草稿,1:发布)
  private Date createTime;//创建时间
  private Long creator;//创建人
  private Date updateTime;//更新时间
  private Long updator;//更新人

  public boolean isCreated() {
    return StringUtils.isNotEmpty(id);
  }

}
