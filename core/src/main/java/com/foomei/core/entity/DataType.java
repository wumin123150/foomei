package com.foomei.core.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;

import com.foomei.common.entity.IdEntity;

/**
 * 数据类型
 *
 * @author walker
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "Core_Data_Type")
@SuppressWarnings("serial")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DataType extends IdEntity {

  public static final String PROP_CODE = "code";
  public static final String PROP_NAME = "name";
  public static final String PROP_REMARK = "remark";
  public static final String PROP_EDITABLE = "editable";

  @NotBlank(message = "代码不能为空")
  @Size(max = 64, message = "代码最大长度为64位")
  private String code;//代码
  @NotBlank(message = "名称不能为空")
  @Size(max = 64, message = "名称最大长度为64位")
  private String name;//名称
  private Integer grade;//层级
  private Boolean editable = true;//是否可修改 (0:不可修改,1:可修改)
  @Size(max = 128, message = "备注最大长度为128位")
  private String remark;//备注

  public DataType(Long id) {
    this.id = id;
  }

}
