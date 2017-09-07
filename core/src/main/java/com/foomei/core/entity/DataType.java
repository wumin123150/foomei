package com.foomei.core.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotBlank;

import com.foomei.common.entity.IdEntity;

/**
 * 数据类型
 * 
 * @author walker
 */
@Getter
@Setter
@ToString(callSuper=true)
@NoArgsConstructor
@Entity
@Table(name = "Core_Data_Type")
@SuppressWarnings("serial")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DataType extends IdEntity {

	public static final String PROP_CODE = "code";
	public static final String PROP_NAME = "name";
	public static final String PROP_REMARK = "remark";
	public static final String PROP_EDITABLE = "editable";

	@NotBlank(message = "编码不能为空")  
	@Size(max = 64, message="编码长度必须在1到64位之间")
	private String code;
	@NotBlank(message = "名称不能为空")  
	@Size(max = 64, message="名称长度必须在1到64位之间")
	private String name;
	private Integer level;
	@Size(max = 128, message="描述长度必须在0到128位之间")
	private String remark;
	@NotNull(message = "修改数据必选")
	private Boolean editable = true;
	
	public DataType(Long id) {
		this.id = id;
	}

}
