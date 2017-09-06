package com.foomei.core.dto;

import com.foomei.common.entity.IdEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ApiModel(description = "数据类型")
public class DataTypeDto {

	private Long id;
	@ApiModelProperty(value="编码", required=true)
	private String code;
	@ApiModelProperty(value="名称", required=true)
	private String name;
	@ApiModelProperty(value="层级", required=true)
	private Integer level;
	@ApiModelProperty(value="备注")
	private String remark;
	@ApiModelProperty(value="是否可修改数据字典")
	private Boolean editable = true;

}
