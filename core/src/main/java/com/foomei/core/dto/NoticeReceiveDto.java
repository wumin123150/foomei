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
  @ApiModelProperty(value = "发送状态(0:发送中,1:已发送,2:发送失败)")
  private Integer sendStatus;
  @ApiModelProperty(value = "阅读状态(0:未读,1:已读)")
  private Integer readStatus;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  @ApiModelProperty(value = "阅读时间")
  private Date readTime;


}
