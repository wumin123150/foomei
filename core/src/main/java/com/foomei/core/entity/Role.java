package com.foomei.core.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.NotBlank;

import com.foomei.common.collection.CollectionExtractor;
import com.foomei.common.entity.IdEntity;
import com.google.common.collect.Lists;

/**
 * 角色.
 *
 * @author walker
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "Core_Role")
@SuppressWarnings("serial")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Role extends IdEntity {
  public static final String PROP_CODE = "code";
  public static final String PROP_NAME = "name";
  public static final String PROP_PERMISSION_LIST = "permissionList";

  @NotBlank(message = "编码不能为空")
  @Size(max = 64, message = "编码长度必须在1到64位之间")
  private String code;
  @NotBlank(message = "名称不能为空")
  @Size(max = 64, message = "名称长度必须在1到64位之间")
  private String name;

  @ManyToMany
  @JoinTable(name = "Core_Role_Permission", joinColumns = {@JoinColumn(name = "roleId")}, inverseJoinColumns = {@JoinColumn(name = "permissionId")})
  @Fetch(FetchMode.SUBSELECT)
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  private List<Permission> permissionList = Lists.newArrayList(); // 有序的关联对象集合

  public Role(Long id) {
    this.id = id;
  }

  @Transient
  public List<String> getPermissions() {
    return CollectionExtractor.extractToList(permissionList, "code");
  }

}
