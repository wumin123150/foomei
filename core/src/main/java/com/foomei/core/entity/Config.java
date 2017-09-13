package com.foomei.core.entity;

import java.util.Map;
import java.util.TreeMap;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.foomei.common.entity.IdEntity;
import com.foomei.common.mapper.JsonMapper;

/**
 * 系统配置
 *
 * @author walker
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "Core_Config")
@SuppressWarnings("serial")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Config extends IdEntity {

  public static final String PROP_CODE = "code";
  public static final String PROP_VALUE = "value";
  public static final String PROP_NAME = "name";
  public static final String PROP_REMARK = "remark";
  public static final String PROP_EDITABLE = "editable";

  @NotBlank(message = "键不能为空")
  @Size(max = 128, message = "键长度必须在1到128位之间")
  private String code;
  @NotBlank(message = "值不能为空")
  @Size(max = 128, message = "值长度必须在1到128位之间")
  private String value;
  @NotBlank(message = "名称不能为空")
  @Size(max = 64, message = "名称长度必须在1到64位之间")
  private String name;
  @NotNull(message = "类型不能为空")
  private Integer type;
  private String params;
  @Size(max = 128, message = "描述长度必须在0到128位之间")
  private String remark;
  @NotNull(message = "修改值必选")
  private Boolean editable = true;

  public Config(Long id) {
    this.id = id;
  }

  @Transient
  @JsonIgnore
  public Map<String, String> getOptions() {
    if (StringUtils.isNotEmpty(params)) {
      JsonMapper jsonMapper = JsonMapper.INSTANCE;
      return jsonMapper.fromJson(params, jsonMapper.buildMapType(TreeMap.class, String.class, String.class));
    }
    return null;
  }

}
