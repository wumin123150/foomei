package com.foomei.core.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.Range;

import com.foomei.common.entity.IdEntity;

/**
 * 权限
 *
 * @author walker
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "Core_Permission")
@SuppressWarnings("serial")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Permission extends IdEntity {
  public static final String PROP_CODE = "code";
  public static final String PROP_NAME = "name";
  public static final String PROP_PRIORITY = "priority";

  private String code;
  private String name;
  @Range(min = 0, max = 10000, message = "序号必须在0到10000之间")
  private Integer priority;

  public Permission(Long id) {
    this.id = id;
  }

}