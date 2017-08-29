package com.foomei.common.web;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;

import com.foomei.common.base.annotation.AutowiredDao;
import com.foomei.common.reflect.ClassUtil;
import com.foomei.common.reflect.ReflectionUtil;

public class ApplicationContextListener implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger log = LoggerFactory.getLogger(ApplicationContextListener.class);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        // root application context
        if(null == applicationContext.getParent()) {
            log.debug(">>>>> spring初始化完毕 <<<<<");
            // spring初始化完毕后，通过反射调用所有使用BaseService注解的initMapper方法
            Map<String, Object> services = applicationContext.getBeansWithAnnotation(Service.class);
            for(Object service : services.values()) {
                Set<Method> methods = ClassUtil.getAnnotatedPublicMethods(service.getClass(), AutowiredDao.class);
                for (Method method : methods) {
                    Class<?> daoClazz = ClassUtil.getClassGenricType(ClassUtil.unwrapCglib(service.getClass()));
                    Object dao = applicationContext.getBean(daoClazz);
                    log.debug(">>>>> {}.setDao()", service.getClass().getName());
                    ReflectionUtil.invokeMethod(service, method, dao);
                }
            }
        }
    }
    
}
