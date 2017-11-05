package com.foomei.core.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "数据字典")
public class DataDictionaryDto {

  private Long id;
  @ApiModelProperty(value = "类型ID", required = true)
  private Long typeId;
  @ApiModelProperty(value = "类型编码", required = true)
  private String typeCode;
  @ApiModelProperty(value = "编码", required = true)
  private String code;
  @ApiModelProperty(value = "名称", required = true)
  private String name;
  @ApiModelProperty(value = "序号", required = true)
  private Integer priority;
  @ApiModelProperty(value = "层级")
  private Integer grade;
  @ApiModelProperty(value = "父ID")
  private Long parentId;
  @ApiModelProperty(value = "备注")
  private String remark;

}
