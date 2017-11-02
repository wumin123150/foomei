package com.foomei.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "角色")
public class RoleVo {

  private Long id;
  @ApiModelProperty(value = "编码", required = true)
  private String code;
  @ApiModelProperty(value = "名称", required = true)
  private String name;
  @ApiModelProperty(value = "权限")
  private List<Long> permissions;

}
