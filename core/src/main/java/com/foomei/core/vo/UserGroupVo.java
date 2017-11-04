package com.foomei.core.vo;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(description = "机构")
public class UserGroupVo {

  private Long id;
  @ApiModelProperty(value = "代码", required = true)
  private String code;
  @ApiModelProperty(value = "名称", required = true)
  private String name;
  @ApiModelProperty(value = "类型(0:公司,1:部门,2:小组,3:其他)", required = true)
  private Integer type;
  @ApiModelProperty(value = "父ID")
  private Long parentId;
  @ApiModelProperty(value = "备注")
  private String remark;
  @ApiModelProperty(value = "角色ID")
  private List<Long> roles = Lists.newArrayList();

}
