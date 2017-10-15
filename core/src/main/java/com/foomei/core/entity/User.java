package com.foomei.core.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.*;
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
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 用户.
 *
 * @author walker
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "Core_User")
@SuppressWarnings("serial")
// 默认的缓存策略.
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User extends IdEntity {
  public static final String PROP_LOGIN_NAME = "loginName";
  public static final String PROP_PASSWORD = "password";
  public static final String PROP_SALT = "salt";
  public static final String PROP_NAME = "name";
  public static final String PROP_QUESTION = "question";
  public static final String PROP_ANSWER = "answer";
  public static final String PROP_SEX = "sex";
  public static final String PROP_BIRTHDAY = "birthday";
  public static final String PROP_MOBILE = "mobile";
  public static final String PROP_EMAIL = "email";
  public static final String PROP_AVATAR = "avatar";
  public static final String PROP_OPEN_ID = "openId";
  public static final String PROP_REGISTER_TIME = "registerTime";
  public static final String PROP_REGISTER_IP = "registerIp";
  public static final String PROP_LAST_LOGIN_TIME = "lastLoginTime";
  public static final String PROP_LAST_LOGIN_IP = "lastLoginIp";
  public static final String PROP_LOGIN_COUNT = "loginCount";
  public static final String PROP_STATUS = "status";
  public static final String PROP_GROUP_LIST = "groupList";
  public static final String PROP_ROLE_LIST = "roleList";

  public static final String STATUS_INACTIVE = "I";
  public static final String STATUS_ACTIVE = "A";
  public static final String STATUS_EXPIRED = "E";
  public static final String STATUS_LOCKED = "L";
  //public static final String STATUS_SUSPENDED = "S";
  public static final String STATUS_TERMINATED = "T";

  public static final String USER_ANNEX_PATH = "/avatar";
  public static final String USER_ANNEX_TYPE = "user";

  @NotBlank(message = "账号不能为空")
  @Size(max = 64, message = "账号最大长度为64位")
  private String loginName;//账号
  private String password;//密码
  private String salt;//散列
  @NotBlank(message = "姓名不能为空")
  @Size(max = 64, message = "姓名最大长度为64位")
  private String name;//姓名
  private String question;//问题
  private String answer;//答案
  private Integer sex;//性别(0:未知,1:男,2:女)
  @DateTimeFormat(pattern="yyyy-MM-dd")
  @Temporal(TemporalType.DATE)
  private Date birthday;//出生日期
  @Size(max = 16, message = "手机最大长度为16位")
  private String mobile;//手机
  @Size(max = 64, message = "邮箱最大长度为64位")
  private String email;//邮箱
  private String avatar;//头像
  private String openId;//微信身份ID
  private Date registerTime;//注册时间
  private String registerIp;//注册IP
  private Date lastLoginTime;//最后登录时间
  private String lastLoginIp;//最后登录IP
  private Integer loginCount;//登录次数
  private String status;//状态(I:未激活,A:正常,E:过期,L:锁定,T:终止)

  // 多对多定义
  @ManyToMany
  @JoinTable(name = "Core_Membership", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "group_id")})
  // Fecth策略定义
  @Fetch(FetchMode.SUBSELECT)
  // 集合按id排序
  @OrderBy("id ASC")
  // 缓存策略
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  private List<UserGroup> groupList = Lists.newArrayList();
  // 多对多定义
  @ManyToMany
  @JoinTable(name = "Core_User_Role", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
  // Fecth策略定义
  @Fetch(FetchMode.SUBSELECT)
  // 集合按id排序
  @OrderBy("id ASC")
  // 缓存策略
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  private List<Role> roleList = Lists.newArrayList(); // 有序的关联对象集合

  @Transient
  private String plainPassword;
  @Transient
  private String plainAnswer;

  public User(Long id) {
    this.id = id;
  }

  public void addLoginCount() {
    if (loginCount == null) {
      loginCount = 1;
    } else {
      loginCount += 1;
    }
  }

  @Transient
  public boolean isEnabled() {
    return STATUS_ACTIVE.equals(status);
  }

  @Transient
  public boolean isAccountInactived() {
    return STATUS_INACTIVE.equals(status);
  }

  @Transient
  public boolean isAccountExpired() {
    return STATUS_EXPIRED.equals(status);
  }

  @Transient
  public boolean isAccountLocked() {
    return STATUS_LOCKED.equals(status);
  }

  @Transient
  public boolean isAccountTerminated() {
    return STATUS_TERMINATED.equals(status);
  }

  @Transient
  public String getRoleNames() {
    return CollectionExtractor.extractToString(roleList, "name", ", ");
  }

  @Transient
  public String getGroupNames() {
    return CollectionExtractor.extractToString(groupList, "name", ", ");
  }

}