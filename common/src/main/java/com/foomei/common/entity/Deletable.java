package com.foomei.common.entity;

public interface Deletable {

  public Boolean getDeleted();

  public void setDeleted(Boolean deleted);

  public void markDeleted();

}
