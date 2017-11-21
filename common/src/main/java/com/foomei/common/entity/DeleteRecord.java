package com.foomei.common.entity;

public interface DeleteRecord {

  public static final String PROP_DEL_FLAG = "delFlag";

  public Boolean getDelFlag();

  public void setDelFlag(Boolean delFlag);

  public void markDeleted();

}
