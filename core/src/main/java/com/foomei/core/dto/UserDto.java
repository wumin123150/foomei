package com.foomei.core.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "用户")
public class UserDto {

  private Long id;
  @ApiModelProperty(value = "名称")
  private String name;
  @ApiModelProperty(value = "性别(0:未知,1:男,2:女)")
  private Integer sex;
  @ApiModelProperty(value = "出生日期")
  private Date birthday;
  @ApiModelProperty(value = "电话")
  private String mobile;
  @ApiModelProperty(value = "邮箱")
  private String email;
  @ApiModelProperty(value = "头像")
  private String avatar;

}
