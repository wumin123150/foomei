package com.foomei.core.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 系统配置
 * 
 * @author walker
 */
@ApiModel(description = "系统配置")
public class ConfigDto {

    private Long id;
    @ApiModelProperty(value="键", required=true)
    private String code;
    @ApiModelProperty(value="值", required=true)
    private String value;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
