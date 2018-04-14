package com.foomei.upms.entity;

import com.foomei.common.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
  public static final String PROP_GRADE = "grade";
  public static final String PROP_PARENT_ID = "parentId";
  public static final String PROP_REMARK = "remark";

  @ManyToOne
  @JoinColumn(name = "type_id")
  private DataType type;//类型
  private String code;//代码
  private String name;//名称
  private Integer priority;//序号
  private Integer grade;//层级
  private Long parentId;//父ID
  private String remark;//备注

}
