package com.foomei.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "通知")
public class NoticeDto {

  private String id;
  @ApiModelProperty(value = "标题")
  private String title;
  @ApiModelProperty(value = "内容")
  private String content;
  @ApiModelProperty(value = "状态")
  private Integer status;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @ApiModelProperty(value = "更新时间")
  private Date updateTime;

}
