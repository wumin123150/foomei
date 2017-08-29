package com.foomei.common.concurrent;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foomei.common.collection.MapUtil;

/**
 * 存储于ThreadLocal的Map, 用于存储上下文.<br/>
 * 
 * 但HashMap<String,Object>的存储其实较为低效，在高性能场景下可改为EnumMap<br/>
 * 
 * 1.先定义枚举类，列举所有可能的Key<br/>
 * 2.替换contextMap的创建语句，见下例<br/>
 * 3.修改put()/get()中key的类型<br/>
 * 
 * <pre>
 * private static ThreadLocal<Map<MyEnum, Object>> contextMap = new ThreadLocal<Map<MyEnum, Object>>() {
 * 	&#64;Override
 * 	protected Map<MyEnum, Object> initialValue() {
 * 		return new EnumMap<MyEnum, Object>(MyEnum.class);
 * 	}
 * };
 * </pre>
 */
public class ThreadLocalContext {
    
    private static final Logger log = LoggerFactory.getLogger(ThreadLocalContext.class);

    private static final ThreadLocal<Map<Object, Object>> resources = new InheritableThreadLocalMap<Map<Object, Object>>();

    public static Map<Object, Object> getResources() {
        return (resources != null) ? resources.get() : null;
    }

    public static void setResources(Map<Object, Object> newResources) {
        if (MapUtil.isEmpty(newResources))
            return;

        resources.get().clear();
        resources.get().putAll(newResources);
    }

    /**
     * 取出ThreadLocal的上下文信息.
     */
    private static Object getValue(Object key) {
        return resources.get().get(key);
    }

    /**
     * 取出ThreadLocal的上下文信息.
     */
    public static <T> T get(Object key) {
        if (log.isTraceEnabled()) {
            String msg = "get() - in thread [" + Thread.currentThread().getName() + "]";
            log.trace(msg);
        }

        Object value = getValue(key);
        if ((value != null) && (log.isTraceEnabled())) {
            String msg = "Retrieved value of type [" + value.getClass().getName() + "] for key [" + key + "] "
                    + "bound to thread [" + Thread.currentThread().getName() + "]";

            log.trace(msg);
        }
        return (T) value;
    }

    /**
     * 取出ThreadLocal的上下文信息.
     */
    public static String getString(Object key) {
        Object value = get(key);
        if (value != null)
            return value.toString();

        return null;
    }

    /**
     * 放入ThreadLocal的上下文信息.
     */
    public static void put(Object key, Object value) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }

        if (value == null) {
            remove(key);
            return;
        }

        resources.get().put(key, value);

        if (log.isTraceEnabled()) {
            String msg = "Bound value of type [" + value.getClass().getName() + "] for key [" + key + "] to thread "
                    + "[" + Thread.currentThread().getName() + "]";

            log.trace(msg);
        }
    }

    public static Object remove(Object key) {
        Object value = resources.get().remove(key);

        if ((value != null) && (log.isTraceEnabled())) {
            String msg = "Removed value of type [" + value.getClass().getName() + "] for key [" + key + "]"
                    + "from thread [" + Thread.currentThread().getName() + "]";

            log.trace(msg);
        }

        return value;
    }

    public static void remove() {
        resources.remove();
    }

    private static final class InheritableThreadLocalMap<T extends Map<Object, Object>> extends
            InheritableThreadLocal<Map<Object, Object>> {
        protected Map<Object, Object> initialValue() {
         // 降低loadFactory减少冲突
            return new HashMap<Object, Object>(16, 0.5f);
        }
    }
    
}
