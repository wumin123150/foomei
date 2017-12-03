package com.foomei.core.dto;

import com.foomei.core.entity.BaseUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "用户组")
public class UserGroupDto {

  private Long id;
  @ApiModelProperty(value = "代码", required = true)
  private String code;
  @ApiModelProperty(value = "名称", required = true)
  private String name;
  @ApiModelProperty(value = "类型(0:公司,1:部门,2:小组,3:其他)", required = true)
  private Integer type;
  @ApiModelProperty(value = "层级", required = true)
  private Integer grade;
  @ApiModelProperty(value = "负责人")
  private BaseUser director;
  @ApiModelProperty(value = "父ID")
  private Long parentId;
  @ApiModelProperty(value = "备注")
  private String remark;

}
