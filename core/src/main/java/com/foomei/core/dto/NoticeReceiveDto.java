package com.foomei.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel(description = "通知")
public class NoticeReceiveDto {

  private String id;
  @ApiModelProperty(value = "通知")
  private NoticeDto notice;
  @ApiModelProperty(value = "状态")
  private Integer status;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @ApiModelProperty(value = "阅读时间")
  private Date readTime;


}
