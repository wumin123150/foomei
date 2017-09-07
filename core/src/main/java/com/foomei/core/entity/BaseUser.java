package com.foomei.core.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.foomei.common.entity.IdEntity;

/**
 * 用户基本信息.
 *
 * @author walker
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@ApiModel(description = "用户")
@Entity
@Table(name = "Core_User")
@SuppressWarnings("serial")
public class BaseUser extends IdEntity {

  public static final String PROP_LOGIN_NAME = "loginName";
  public static final String PROP_NAME = "name";
  public static final String PROP_MOBILE = "mobile";
  public static final String PROP_EMAIL = "email";
  public static final String PROP_AVATAR = "avatar";
  public static final String PROP_OPEN_ID = "openId";
  public static final String PROP_STATUS = "status";

  public static final String STATUS_INACTIVE = "I";
  public static final String STATUS_ACTIVE = "A";
  public static final String STATUS_EXPIRED = "E";
  public static final String STATUS_LOCKED = "L";
  //public static final String STATUS_SUSPENDED = "S";
  public static final String STATUS_TERMINATED = "T";

  @ApiModelProperty(value = "用户名")
  private String loginName;
  @ApiModelProperty(value = "姓名")
  private String name;
  @ApiModelProperty(value = "手机")
  private String mobile;
  @ApiModelProperty(value = "邮箱")
  private String email;
  @ApiModelProperty(value = "头像")
  private String avatar;
  @ApiModelProperty(value = "微信openId")
  private String openId;
  @ApiModelProperty(value = "状态")
  private String status;

  public BaseUser(Long id) {
    this.id = id;
  }

  public BaseUser(Long id, String loginName, String name) {
    this.id = id;
    this.loginName = loginName;
    this.name = name;
  }

}