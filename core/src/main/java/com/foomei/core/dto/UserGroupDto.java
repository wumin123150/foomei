package com.foomei.core.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

import lombok.Data;

import com.google.common.collect.Lists;

@Data
@ApiModel(description = "用户组")
public class UserGroupDto {

	private Long id;
	@ApiModelProperty(value="编码", required=true)
	private String code;
	@ApiModelProperty(value="名称", required=true)
	private String name;
	@ApiModelProperty(value="类型", required=true)
    private Integer type;
	@ApiModelProperty(value="层级", required=true)
	private Integer level;
	@ApiModelProperty(value="角色ID")
	private List<Long> roleIds = Lists.newArrayList();
	@ApiModelProperty(value="父ID")
	private Long parentId;
	@ApiModelProperty(value="备注")
	private String remark;

}
