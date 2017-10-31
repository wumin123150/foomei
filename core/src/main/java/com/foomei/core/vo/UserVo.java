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
  @ApiModelProperty(value = "账号")
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
  @ApiModelProperty(value = "角色")
  List<Long> roles;
  @ApiModelProperty(value = "机构")
  List<Long> groups;

}
