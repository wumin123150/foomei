package com.foomei.core.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.foomei.core.entity.DataDictionary;
import com.foomei.core.entity.UserGroup;

@ApiModel(description = "树节点")
public class TreeNodeDto {

	private String id;
	@ApiModelProperty(value="父ID")
	private String pId;
	@ApiModelProperty(value="名称")
	private String name;
	@ApiModelProperty(value="展开状态")
	private Boolean open;
	@ApiModelProperty(value="分支节点")
	private Boolean isParent;
	
	public TreeNodeDto(DataDictionary dataDictionary) {
		this.id = String.valueOf(dataDictionary.getId());
		if(dataDictionary.getParentId() != null) {
			this.pId = String.valueOf(dataDictionary.getParentId());
		}
		this.name = dataDictionary.getName()+"("+dataDictionary.getCode()+")";
		this.isParent = !dataDictionary.getItem();
	}
	
	public TreeNodeDto(UserGroup userGroup) {
		this.id = String.valueOf(userGroup.getId());
		if(userGroup.getParentId() != null) {
			this.pId = String.valueOf(userGroup.getParentId());
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}

	public Boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(Boolean isParent) {
		this.isParent = isParent;
	}
	
}
