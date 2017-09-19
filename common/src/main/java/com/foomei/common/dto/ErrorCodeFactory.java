package com.foomei.common.dto;

public class ErrorCodeFactory {

  //常见的错误
  public static final ErrorCode BAD_REQUEST = new ErrorCode(400, "参数错误");
  public static final ErrorCode UNAUTHORIZED = new ErrorCode(401, "未经许可");
  public static final ErrorCode FORBIDDEN = new ErrorCode(403, "禁止访问");
  public static final ErrorCode INTERNAL_SERVER_ERROR = new ErrorCode(500, "系统错误");

}
