package com.foomei.common.dto;

import java.util.List;

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
    private int errorCode;
    private String message;
    private T data;
    
    public ResponseResult() {
        this("success");
    }
    
    public ResponseResult(T data) {
        this();
        setData(data);
    }
    
    public ResponseResult(String message) {
        this.success = true;
        this.errorCode = SUCCESS_CODE;
        this.message = message;
    }
    
    public static <T> ResponseResult createSuccess(T data) {
        return new ResponseResult(data);
    }
    
    public static <S, T> ResponseResult createSuccess(S data, Class<T> targetClass) {
        return new ResponseResult(BeanMapper.map(data, targetClass));
    }
    
    public static <S, T> ResponseResult createSuccess(Iterable<S> sourceList, Class<S> sourceClass, Class<T> targetClass) {
        return new ResponseResult(BeanMapper.mapList(sourceList, sourceClass, targetClass));
    }
    
    public static <S, T> ResponseResult createSuccess(Page<S> sourcePage, Class<S> sourceClass, Class<T> targetClass) {
        List<T> content = BeanMapper.mapList(sourcePage.getContent(), sourceClass, targetClass);
        return new ResponseResult(new PageImpl(content, (Pageable)ReflectionUtil.getProperty(sourcePage, "pageable"), sourcePage.getTotalElements()));
    }
    
    public ResponseResult(ErrorCode errorCode) {
        this.success = false;
        this.errorCode = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
    
    public ResponseResult(ErrorCode errorCode, String message) {
        this.success = false;
        this.errorCode = errorCode.getCode();
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
        if(ex instanceof ServiceException) {
            ResponseResult result = createError(ErrorCodeFactory.INTERNAL_SERVER_ERROR, ex.getMessage());
            result.setData(ExceptionUtil.toStringWithLocation(ex, ClassUtil.getPackageName(ResponseResult.class.getName(), 2)));
            return result;
        }
        
        if(ex instanceof BindException) {
            ResponseResult result = createParamError(((BindException)ex).getBindingResult());
            return result;
        }
        
        if(ex instanceof MethodArgumentNotValidException) {
            ResponseResult result = createParamError(((MethodArgumentNotValidException)ex).getBindingResult());
            return result;
        }
        
        ResponseResult result = createError(ErrorCodeFactory.INTERNAL_SERVER_ERROR);
        result.setData(ExceptionUtil.toStringWithLocation(ex, ClassUtil.getPackageName(ResponseResult.class.getName(), 2)));
        return result;
    }
    
    public boolean isSuccess() {
        return success;
    }
    
    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
