package com.foomei.core.entity;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
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

import com.foomei.common.entity.IdEntity;
import com.google.common.collect.Lists;

/**
 * 用户组
 *
 * @author walker
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "Core_User_Group")
@SuppressWarnings("serial")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserGroup extends IdEntity {

  public static final String PROP_CODE = "code";
  public static final String PROP_NAME = "name";
  public static final String PROP_TYPE = "type";
  public static final String PROP_LEVEL = "level";
  public static final String PROP_PATH = "path";
  public static final String PROP_PARENT_ID = "parentId";
  public static final String PROP_REMARK = "remark";
  public static final String PROP_DEL_FLAG = "delFlag";

  public static final String PATH_SPLIT = "/";

  @NotBlank(message = "编码不能为空")
  @Size(max = 64, message = "编码长度必须在1到64位之间")
  private String code;
  @NotBlank(message = "名称不能为空")
  @Size(max = 64, message = "名称长度必须在1到64位之间")
  private String name;
  private Integer type;
  @ApiModelProperty(value = "层级")
  private Integer level;
  private String path;
  private Long parentId;
  @Size(max = 128, message = "描述长度必须在0到128位之间")
  private String remark;
  private Boolean delFlag;

  public UserGroup(Long id) {
    this.id = id;
  }

  // 多对多定义
  @ManyToMany
  @JoinTable(name = "Core_Group_Role", joinColumns = {@JoinColumn(name = "group_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
  // Fecth策略定义
  @Fetch(FetchMode.SUBSELECT)
  // 集合按id排序
  @OrderBy("id ASC")
  // 缓存策略
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  private List<Role> roleList = Lists.newArrayList();

}
