package com.foomei.common.dto;

import java.util.List;

import com.baidu.unbiz.fluentvalidator.ComplexResult;
import com.baidu.unbiz.fluentvalidator.ValidationError;
import com.baidu.unbiz.fluentvalidator.util.CollectionUtil;
import com.foomei.common.collection.CommonCollections;
import com.foomei.common.collection.ListUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.foomei.common.base.ExceptionUtil;
import com.foomei.common.mapper.BeanMapper;
import com.foomei.common.reflect.ClassUtil;
import com.foomei.common.reflect.ReflectionUtil;
import com.foomei.common.service.impl.ServiceException;

public class ResponseResult<T> {

  public final static int SUCCESS_CODE = 0;

  public final static ResponseResult SUCCEED = new ResponseResult<>();
  //    public final static ResponseResult LOGIN = new ResponseResult<>(ERR_LOGIN, "login");
  public final static ResponseResult UNAUTHORIZED = createError(ErrorCodeFactory.UNAUTHORIZED);//401
  public final static ResponseResult FORBIDDEN = createError(ErrorCodeFactory.FORBIDDEN);//403

  private boolean success;
  private int code;
  private String message;
  private long total;
  private T data;

  public ResponseResult() {
    this("success");
  }

  public ResponseResult(T data) {
    this();
    setData(data);
  }

  public ResponseResult(T data, Long total) {
    this();
    setData(data);
    setTotal(total);
  }

  public ResponseResult(String message) {
    this.success = true;
    this.code = SUCCESS_CODE;
    this.message = message;
  }

  public static <T> ResponseResult<T> createSuccess(T data) {
    return new ResponseResult(data);
  }

  public static <T> ResponseResult<List<T>> createSuccess(List<T> sourceList) {
    return new ResponseResult(sourceList, Long.valueOf(sourceList.size()));
  }

  public static <T> ResponseResult<List<T>> createSuccess(List<T> sourceList, Long total) {
    return new ResponseResult(sourceList, total);
  }

  public static <S, T> ResponseResult<T> createSuccess(S data, Class<T> targetClass) {
    return new ResponseResult(BeanMapper.map(data, targetClass));
  }

  public static <S, T> ResponseResult<List<T>> createSuccess(List<S> sourceList, Class<S> sourceClass, Class<T> targetClass) {
    return new ResponseResult(BeanMapper.mapList(sourceList, sourceClass, targetClass), Long.valueOf(sourceList.size()));
  }

  public static <S, T> ResponseResult<List<T>> createSuccess(List<S> sourceList, Long total, Class<S> sourceClass, Class<T> targetClass) {
    return new ResponseResult(BeanMapper.mapList(sourceList, sourceClass, targetClass), total);
  }

  public static <S, T> ResponseResult<Page<T>> createSuccess(Page<S> sourcePage, Class<S> sourceClass, Class<T> targetClass) {
    List<T> content = BeanMapper.mapList(sourcePage.getContent(), sourceClass, targetClass);
    return new ResponseResult(new PageImpl(content, (Pageable) ReflectionUtil.getProperty(sourcePage, "pageable"), sourcePage.getTotalElements()));
  }

  public ResponseResult(ErrorCode errorCode) {
    this.success = false;
    this.code = errorCode.getCode();
    this.message = errorCode.getMessage();
  }

  public ResponseResult(ErrorCode errorCode, String message) {
    this.success = false;
    this.code = errorCode.getCode();
    this.message = message;
  }

  public static ResponseResult createError(ErrorCode errorCode) {
    return new ResponseResult(errorCode);
  }

  public static ResponseResult createError(ErrorCode errorCode, String message) {
    return new ResponseResult(errorCode, message);
  }

  public static ResponseResult createParamError(String message) {
    return new ResponseResult(ErrorCodeFactory.BAD_REQUEST, message);
  }

  public static ResponseResult createParamError(ComplexResult complexResult) {
    ResponseResult result = createError(ErrorCodeFactory.BAD_REQUEST);
    result.setData(complexResult.getErrors());
    return result;
  }

  public static ResponseResult createParamError(BindingResult bindingResult) {
    ResponseResult result = createError(ErrorCodeFactory.BAD_REQUEST);
    result.setData(bindingResult.getFieldErrors());
    return result;
  }

  public static ResponseResult createParamError(BindingResult bindingResult, String message) {
    ResponseResult result = createError(ErrorCodeFactory.BAD_REQUEST, message);
    result.setData(bindingResult.getFieldErrors());
    return result;
  }

  public static ResponseResult create4Exception(Exception ex) {
    if (ex instanceof ServiceException) {
      ResponseResult result = createError(ErrorCodeFactory.INTERNAL_SERVER_ERROR, ex.getMessage());
      result.setData(ExceptionUtil.toStringWithLocation(ex, ClassUtil.getPackageName(ResponseResult.class.getName(), 2)));
      return result;
    }

    if (ex instanceof BindException) {
      ResponseResult result = createParamError(((BindException) ex).getBindingResult());
      return result;
    }

    if (ex instanceof MethodArgumentNotValidException) {
      ResponseResult result = createParamError(((MethodArgumentNotValidException) ex).getBindingResult());
      return result;
    }

    ResponseResult result = createError(ErrorCodeFactory.INTERNAL_SERVER_ERROR);
    result.setData(ExceptionUtil.toStringWithLocation(ex, ClassUtil.getPackageName(ResponseResult.class.getName(), 2)));
    return result;
  }

  public boolean isSuccess() {
    return success;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public long getTotal() {
    return total;
  }

  public void setTotal(long total) {
    this.total = total;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

}
