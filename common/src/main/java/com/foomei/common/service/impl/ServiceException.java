package com.foomei.common.service.impl;

import com.foomei.common.dto.ErrorCode;

public class ServiceException extends RuntimeException {

  private static final long serialVersionUID = -8634700792767837033L;

  public ErrorCode errorCode;

  public ServiceException(String message) {
    super(message);
  }

  public ServiceException(String message, Throwable cause) {
    super(message, cause);
  }

  public ServiceException(ErrorCode errorCode) {
    super(errorCode.getMessage());
  }

  public ServiceException(String message, ErrorCode errorCode) {
    super(message);
    this.errorCode = errorCode;
  }

}
