package com.foomei.core.entity;

import com.foomei.common.entity.IdEntity;
import com.foomei.common.mapper.JsonMapper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Map;
import java.util.TreeMap;

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

  private String code;//键
  private String value;//值
  private String name;//名称
  private Integer type;//类型(0:Input输入框,1:Textarea文本框,2:Radio单选框,3:Checkbox多选框,4:Select单选框,5:Select多选框)
  private String params;//参数
  private Boolean editable = true;//是否可修改 (0:不可修改,1:可修改)
  private String remark;//备注

  public Config(Long id) {
    this.id = id;
  }

  @Transient
  public Map getOptions() {
    if (StringUtils.isNotEmpty(params)) {
      return JsonMapper.INSTANCE.fromJson(params, Map.class);
    }
    return null;
  }

}
