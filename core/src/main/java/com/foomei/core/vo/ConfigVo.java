package com.foomei.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@ApiModel(description = "系统配置")
public class ConfigVo {

  private Long id;
  @ApiModelProperty(value = "名称", required = true)
  private String name;
  @ApiModelProperty(value = "键", required = true)
  private String code;
  @ApiModelProperty(value = "值", required = true)
  private String value;
  @ApiModelProperty(value = "类型(0:Input输入框,1:Textarea文本框,2:Radio单选框,3:Checkbox多选框,4:Select单选框,5:Select多选框)", required = true)
  private Integer type;
  @ApiModelProperty(value = "参数")
  private String params;
  @ApiModelProperty(value = "是否可修改(0:不可修改,1:可修改)", required = true)
  private Boolean editable = false;
  @ApiModelProperty(value = "备注")
  private String remark;

}
