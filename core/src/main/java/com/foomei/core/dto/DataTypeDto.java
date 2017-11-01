package com.foomei.core.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "数据类型")
public class DataTypeDto {

  private Long id;
  @ApiModelProperty(value = "编码", required = true)
  private String code;
  @ApiModelProperty(value = "名称", required = true)
  private String name;
  @ApiModelProperty(value = "层级", required = true)
  private Integer grade;
  @ApiModelProperty(value = "是否可修改数据(0:不可修改,1:可修改)", required = true)
  private Boolean editable;
  @ApiModelProperty(value = "备注")
  private String remark;

}
