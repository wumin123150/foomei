package com.foomei.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel(description = "用户")
public class UserVo {

  private Long id;
  @ApiModelProperty(value = "账号", required = true)
  private String loginName;
  @ApiModelProperty(value = "密码")
  private String password;
  @ApiModelProperty(value = "姓名")
  private String name;
  @ApiModelProperty(value = "性别(0:保密,1:男,2:女)")
  private Integer sex;
  @ApiModelProperty(value = "出生日期")
  private Date birthday;
  @ApiModelProperty(value = "手机")
  private String mobile;
  @ApiModelProperty(value = "邮箱")
  private String email;
  @ApiModelProperty(value = "状态(I:未激活,A:正常,E:过期,L:锁定,T:终止)", required = true)
  private String status;
  @ApiModelProperty(value = "头像ID")
  private String avatarId;
  @ApiModelProperty(value = "角色")
  private List<Long> roles;
  @ApiModelProperty(value = "机构")
  private List<Long> groups;

}
