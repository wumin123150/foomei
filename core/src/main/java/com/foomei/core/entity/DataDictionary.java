package com.foomei.core.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import com.foomei.common.entity.IdEntity;

/**
 * 数据字典
 *
 * @author walker
 */
@Getter
@Setter
@ToString(callSuper = true)
@ApiModel(description = "数据字典")
@Entity
@Table(name = "Core_Data_Dictionary")
@SuppressWarnings("serial")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DataDictionary extends IdEntity {

  public static final String PROP_TYPE = "type";
  public static final String PROP_CODE = "code";
  public static final String PROP_NAME = "name";
  public static final String PROP_PRIORITY = "priority";
  public static final String PROP_LEVEL = "level";
  public static final String PROP_ITEM = "item";
  public static final String PROP_PARENT_ID = "parentId";
  public static final String PROP_REMARK = "remark";

  @NotNull(message = "数据类型不能为空")
  @ManyToOne
  @JoinColumn(name = "type_id")
  private DataType type;
  @NotBlank(message = "编码不能为空")
  @Size(max = 64, message = "编码长度必须在1到64位之间")
  private String code;
  @NotBlank(message = "名称不能为空")
  @Size(max = 64, message = "名称长度必须在1到64位之间")
  private String name;
  @NotNull(message = "序号不能为空")
  @Range(min = 0, max = 10000, message = "序号必须在0到10000之间")
  private Integer priority;
  private Integer level;
  @NotNull(message = "类型不能为空")
  @Column(name = "is_item")
  private Boolean item;
  private Long parentId;
  @Size(max = 128, message = "描述长度必须在0到128位之间")
  private String remark;

}
