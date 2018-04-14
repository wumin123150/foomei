package com.foomei.common.persistence;

import com.baomidou.mybatisplus.enums.SqlLike;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.foomei.common.collection.CollectionUtil;
import com.foomei.common.persistence.search.BooleanOperator;
import com.foomei.common.persistence.search.SearchFilter;
import com.foomei.common.persistence.search.SearchRequest;
import com.foomei.common.persistence.search.SimpleFilter;
import com.foomei.common.reflect.ClassUtil;
import com.foomei.common.reflect.ReflectionUtil;
import com.foomei.common.time.DateFormatUtil;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class DynamicWrapper {

  @SuppressWarnings({"unchecked", "rawtypes"})
  public static <T> EntityWrapper bySearchRequest(final SearchRequest searchRequest, final Class<T> entityClazz) {
    EntityWrapper wrapper = filterTo(searchRequest.getOperator(), searchRequest.getSearchFilters(), entityClazz);
    if (searchRequest.getSort() != null) {
      for (Iterator iterator = searchRequest.getSort().iterator(); iterator.hasNext(); ) {
        Sort.Order order = (Sort.Order) iterator.next();
        wrapper.orderBy(order.getProperty(), !Sort.Direction.DESC.equals(order.getDirection()));
      }
    }
    return wrapper;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public static <T> EntityWrapper filterTo(final BooleanOperator operator, final List<SearchFilter> searchFilters, final Class<T> entityClazz) {
    EntityWrapper wrapper = new EntityWrapper();

    T newEntity = ReflectionUtil.invokeConstructor(entityClazz);
    wrapper.setEntity(newEntity);

    if (CollectionUtil.isNotEmpty(searchFilters)) {
      for (SearchFilter searchFilter : searchFilters) {
        if (searchFilter instanceof SimpleFilter) {
          if(operator == BooleanOperator.OR) {
            wrapper.or();
          }

          SimpleFilter filter = (SimpleFilter) searchFilter;

          // nested path translate, 如Task的名为"user.name"的filedName, 转换为Task.user.name属性
          Field field = ClassUtil.getAccessibleField(entityClazz, filter.getPropertyName());
          Class fieldClazz = field != null ? field.getClass() : String.class;

          Object value = null;

          // logic operator
          switch (filter.getOperator()) {
            case EQ:
              value = stringTo(filter.getValue(), fieldClazz);
              wrapper.eq(filter.getPropertyName(), value);
              break;
            case NE:
              value = stringTo(filter.getValue(), fieldClazz);
              wrapper.ne(filter.getPropertyName(), value);
              break;
            case LT:
              value = stringTo(filter.getValue(), fieldClazz);
              wrapper.lt(filter.getPropertyName(), value);
              break;
            case LE:
              value = stringTo(filter.getValue(), fieldClazz);
              wrapper.le(filter.getPropertyName(), value);
              break;
            case GT:
              value = stringTo(filter.getValue(), fieldClazz);
              wrapper.gt(filter.getPropertyName(), value);
              break;
            case GE:
              value = stringTo(filter.getValue(), fieldClazz);
              wrapper.ge(filter.getPropertyName(), value);
              break;
            case SW:
              wrapper.like(filter.getPropertyName(), toString(filter.getValue()), SqlLike.RIGHT);
              break;
            case SN:
              wrapper.notLike(filter.getPropertyName(), toString(filter.getValue()), SqlLike.RIGHT);
              break;
            case EW:
              wrapper.like(filter.getPropertyName(), toString(filter.getValue()), SqlLike.LEFT);
              break;
            case EN:
              wrapper.notLike(filter.getPropertyName(), toString(filter.getValue()), SqlLike.LEFT);
              break;
            case CN:
              wrapper.like(filter.getPropertyName(), toString(filter.getValue()));
              break;
            case NC:
              wrapper.notLike(filter.getPropertyName(), toString(filter.getValue()));
              break;
            case NU:
              wrapper.isNull(filter.getPropertyName());
              break;
            case NN:
              wrapper.isNotNull(filter.getPropertyName());
              break;
            case IN:
              List<Object> in = new ArrayList();
              for (Object val : (List) filter.getValue()) {
                in.add(stringTo(val, fieldClazz));
              }
              wrapper.in(filter.getPropertyName(), in);
              break;
            case NI:
              List<Object> nin = new ArrayList();
              for (Object val : (List) filter.getValue()) {
                nin.add(stringTo(val, fieldClazz));
              }
              wrapper.notIn(filter.getPropertyName(), nin);
              break;
          }
        }
      }
    }

    return wrapper;
  }

  private static Object stringTo(Object value, final Class clazz) {
    if (value instanceof String) {
      if (Boolean.class.isAssignableFrom(clazz)) {
        return Boolean.valueOf((String) value);
      }
      if (Short.class.isAssignableFrom(clazz)) {
        return Short.valueOf((String) value);
      }
      if (Integer.class.isAssignableFrom(clazz)) {
        return Integer.valueOf((String) value);
      }
      if (Long.class.isAssignableFrom(clazz)) {
        return Long.valueOf((String) value);
      }
      if (Float.class.isAssignableFrom(clazz)) {
        return Float.valueOf((String) value);
      }
      if (Double.class.isAssignableFrom(clazz)) {
        return Double.valueOf((String) value);
      }
      if (BigDecimal.class.isAssignableFrom(clazz)) {
        return new BigDecimal((String) value);
      }
      if (Date.class.isAssignableFrom(clazz)) {
        return DateFormatUtil.parseShortDate((String) value);
      }
    }
    return value;
  }

  private static String toString(Object value) {
    if (value == null) {
      return "";
    }
    return String.valueOf(value);
  }

}
