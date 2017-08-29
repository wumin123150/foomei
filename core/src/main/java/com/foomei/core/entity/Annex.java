package com.foomei.core.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.foomei.common.entity.UuidEntity;

/**
 * 附件
 * 
 * @author walker
 */
@Getter
@Setter
@ToString(callSuper=true)
@Entity
@Table(name = "Core_Annex")
@SuppressWarnings("serial")
public class Annex extends UuidEntity {

	public static final String PROP_OBJECT_ID = "objectId";
	public static final String PROP_OBJECT_TYPE = "objectType";
	public static final String PROP_PATH = "path";
	public static final String PROP_NAME = "name";
	public static final String PROP_TYPE = "type";
	public static final String PROP_CREATE_TIME = "createTime";
	public static final String PROP_CREATOR = "creator";

	private String objectId;
	private String objectType;
	private String path;
	private String name;
	private String type;
	private Date createTime;
	private Long creator;

}
