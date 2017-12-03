package com.foomei.core.web;

import com.foomei.common.base.ObjectUtil;
import com.foomei.common.base.annotation.LogIgnore;
import com.foomei.common.collection.ArrayUtil;
import com.foomei.common.collection.ListUtil;
import com.foomei.common.collection.MapUtil;
import com.foomei.common.mapper.JsonMapper;
import com.foomei.common.net.IPUtil;
import com.foomei.common.net.RequestUtil;
import com.foomei.core.entity.Log;
import com.foomei.core.service.LogService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 日志记录AOP实现
 */
@Aspect
@Component
public class LogAspect {

  private static Logger logger = LoggerFactory.getLogger(LogAspect.class);

  JsonMapper jsonMapper = new JsonMapper();

  // 开始时间
  private long startTime = 0L;
  // 结束时间
  private long endTime = 0L;

  @Autowired
  LogService logService;

  @Before("@annotation(io.swagger.annotations.ApiOperation)")
  // @Before("execution(* *..controller.*.*(..))")
  public void doBeforeInServiceLayer(JoinPoint joinPoint) {
    logger.debug("doBeforeInServiceLayer");
    startTime = System.currentTimeMillis();
  }

  @After("@annotation(io.swagger.annotations.ApiOperation)")
  // @After("execution(* *..controller.*.*(..))")
  public void doAfterInServiceLayer(JoinPoint joinPoint) {
    logger.debug("doAfterInServiceLayer");
  }

  @Around("@annotation(io.swagger.annotations.ApiOperation)")
  // @Around("execution(* *..controller.*.*(..))")
  public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
    // 获取request
    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

    Log log = new Log();
    // 从注解中获取操作名称、获取响应结果
    Object result = pjp.proceed();
    Signature signature = pjp.getSignature();
    MethodSignature methodSignature = (MethodSignature) signature;
    Method method = methodSignature.getMethod();
    LogIgnore logIgnore = method.getAnnotation(LogIgnore.class);
    if (method.isAnnotationPresent(ApiOperation.class) && !(logIgnore != null && StringUtils.equals("method", logIgnore.value()))) {
      ApiOperation api = method.getAnnotation(ApiOperation.class);
      log.setDescription(api.value());
    } else {
      return result;
    }

    endTime = System.currentTimeMillis();
    if (logger.isDebugEnabled()) {
      logger.debug("doAround>>>result={},耗时：{}", result, endTime - startTime);
    }

    List<String> excludes = ListUtil.newArrayList();
    if (logIgnore != null && StringUtils.equals("field", logIgnore.value())) {
      excludes = ArrayUtil.asList(logIgnore.excludes());
    }

    Map<String, Object> parameter = RequestUtil.getParameters(request, excludes);
        /*
         * 使用multipart/form-data的接口，必须增加HttpServletRequest参数
         * 因为通过CommonsMultipartResolver转换成MultipartHttpServletRequest后，就无法从原request中获取参数
         */
    if (ServletFileUpload.isMultipartContent(request)) {
      MultipartHttpServletRequest multiRequest = null;
      Object[] args = pjp.getArgs();
      for (int i = 0; i < args.length; i++) {
        if (args[i] instanceof MultipartHttpServletRequest) {
          multiRequest = (MultipartHttpServletRequest) args[i];

          parameter = RequestUtil.getParameters(multiRequest, excludes);

          Map<String, MultipartFile> files = multiRequest.getFileMap();
          for (Map.Entry<String, MultipartFile> entry : files.entrySet()) {
            MultipartFile file = entry.getValue();
            if (file.isEmpty()) {
              parameter.put(entry.getKey(), null);
            } else {
              parameter.put(entry.getKey(), MapUtil.newHashMap(new String[]{"filename", "size"}, new Object[]{file.getOriginalFilename(), file.getSize()}));
            }
          }

          break;
        }
      }
    }

    log.setIp(IPUtil.getIp(request));
    log.setUrl(ObjectUtil.toPrettyString(request.getRequestURL()));
    log.setMethod(request.getMethod());
    log.setUserAgent(request.getHeader("User-Agent"));
    log.setParameter(jsonMapper.toJson(parameter));
    log.setResult(result instanceof String ? (String) result : jsonMapper.toJson(result));
    log.setLogTime(new Date(startTime));
    log.setSpendTime((int) (endTime - startTime));
    log.setUsername(ObjectUtil.toPrettyString(request.getUserPrincipal()));
    logService.save(log);

    return result;
  }
}
