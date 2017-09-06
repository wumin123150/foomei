package com.foomei.core.dto;

import com.foomei.common.entity.IdEntity;
import com.foomei.core.entity.DataType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ApiModel(description = "数据字典")
public class DataDictionaryDto {

	private Long id;
	@ApiModelProperty(value="数据类型ID", required=true)
	private Long typeId;
	@ApiModelProperty(value="编码", required=true)
	private String code;
	@ApiModelProperty(value="名称", required=true)
	private String name;
	@ApiModelProperty(value="序号", required=true)
	private Integer priority;
	@ApiModelProperty(value="层级")
	private Integer level;
	@ApiModelProperty(value="类型", required=true)
	private Boolean item;
	private Long parentId;
	@ApiModelProperty(value="备注")
	private String remark;

}
