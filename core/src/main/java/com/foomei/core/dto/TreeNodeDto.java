package com.foomei.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.foomei.core.entity.DataDictionary;
import com.foomei.core.entity.UserGroup;
import lombok.Data;

@Data
@ApiModel(description = "树节点")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TreeNodeDto {

  private String id;
  @ApiModelProperty(value = "父ID")
  private String pId;
  @ApiModelProperty(value = "名称")
  private String name;
  @ApiModelProperty(value = "分支节点")
  private Boolean isParent;

  public TreeNodeDto(DataDictionary dataDictionary) {
    this.id = String.valueOf(dataDictionary.getId());
    if (dataDictionary.getParentId() != null) {
      this.pId = String.valueOf(dataDictionary.getParentId());
    }
    this.name = dataDictionary.getName() + "(" + dataDictionary.getCode() + ")";
    this.isParent = !dataDictionary.getItem();
  }

  public TreeNodeDto(UserGroup userGroup) {
    this.id = String.valueOf(userGroup.getId());
    if (userGroup.getParentId() != null) {
      this.pId = String.valueOf(userGroup.getParentId());
    } else {
      this.pId = "0";
    }
    this.name = userGroup.getName();
    this.isParent = true;
  }

  public TreeNodeDto(String id, String parentId, String name, Boolean isParent) {
    this.id = id;
    this.pId = parentId;
    this.name = name;
    this.isParent = isParent;
  }

}
