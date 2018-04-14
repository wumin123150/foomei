package com.foomei.common.entity;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 统一定义id的entity基类.
 * <p>
 * 基类统一定义id的属性名称、数据类型、列名映射及生成策略.
 * 子类可重载getId()函数重定义id的列名映射和生成策略.
 *
 * @author walker
 */
@MappedSuperclass
public abstract class IdEntity implements Serializable {

  private static final long serialVersionUID = 1886787906868589321L;
  public static final String PROP_ID = "id";

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @TableId(type = IdType.AUTO)
  protected Long id;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public boolean equals(Object o) {
    if (!(o instanceof IdEntity)) {
      return false;
    }
    IdEntity another = (IdEntity) o;
    return new EqualsBuilder().append(this.getId(), another.getId()).isEquals();
  }

  public int hashCode() {
    return new HashCodeBuilder().append(id).hashCode();
  }

  public String toString() {
    return "(id=" + getId() + ")";
  }

}
