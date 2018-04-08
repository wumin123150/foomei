package com.foomei.common.persistence;

import com.foomei.common.collection.CollectionUtil;
import com.foomei.common.persistence.search.BooleanOperator;
import com.foomei.common.persistence.search.SearchFilter;
import com.foomei.common.persistence.search.SearchRequest;
import com.foomei.common.persistence.search.SimpleFilter;
import com.foomei.common.reflect.ClassUtil;
import com.foomei.common.time.DateFormatUtil;
import org.springframework.data.domain.Sort.Order;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class DynamicExample {

  @SuppressWarnings({"unchecked", "rawtypes"})
  public static <T> Example bySearchRequest(final SearchRequest searchRequest, final Class<T> entityClazz) {
    Example example = filterTo(searchRequest.getOperator(), searchRequest.getSearchFilters(), entityClazz);
    if (searchRequest.getSort() != null) {
      StringBuilder builder = new StringBuilder();
      for (Iterator iterator = searchRequest.getSort().iterator(); iterator.hasNext(); ) {
        Order order = (Order) iterator.next();
        builder.append(",").append(order.getProperty()).append(" ").append(order.getDirection().name());
      }
      if (builder.length() > 0) {
        example.setOrderByClause(builder.substring(1));
      }
    }
    return example;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public static <T> Example filterTo(final BooleanOperator operator, final List<SearchFilter> searchFilters, final Class<T> entityClazz) {
    Example example = new Example(entityClazz);

    Example.Criteria criteria = null;
    if (CollectionUtil.isNotEmpty(searchFilters)) {
      for (SearchFilter searchFilter : searchFilters) {
        if (operator == BooleanOperator.OR || criteria == null) {
          criteria = example.or();
        }

        if (searchFilter instanceof SimpleFilter) {

          SimpleFilter filter = (SimpleFilter) searchFilter;

          // nested path translate, 如Task的名为"user.name"的filedName, 转换为Task.user.name属性
          Field field = ClassUtil.getAccessibleField(entityClazz, filter.getPropertyName());
          Class fieldClazz = field != null ? field.getClass() : String.class;

          Object value = null;

          // logic operator
          switch (filter.getOperator()) {
            case EQ:
              value = stringTo(filter.getValue(), fieldClazz);
              criteria.andEqualTo(filter.getPropertyName(), value);
              break;
            case NE:
              value = stringTo(filter.getValue(), fieldClazz);
              criteria.andNotEqualTo(filter.getPropertyName(), value);
              break;
            case LT:
              value = stringTo(filter.getValue(), fieldClazz);
              criteria.andLessThan(filter.getPropertyName(), value);
              break;
            case LE:
              value = stringTo(filter.getValue(), fieldClazz);
              criteria.andLessThanOrEqualTo(filter.getPropertyName(), value);
              break;
            case GT:
              value = stringTo(filter.getValue(), fieldClazz);
              criteria.andGreaterThan(filter.getPropertyName(), value);
              break;
            case GE:
              value = stringTo(filter.getValue(), fieldClazz);
              criteria.andGreaterThanOrEqualTo(filter.getPropertyName(), value);
              break;
            case SW:
              criteria.andLike(filter.getPropertyName(), filter.getValue() + "%");
              break;
            case SN:
              criteria.andNotLike(filter.getPropertyName(), filter.getValue() + "%");
              break;
            case EW:
              criteria.andLike(filter.getPropertyName(), "%" + filter.getValue());
              break;
            case EN:
              criteria.andNotLike(filter.getPropertyName(), "%" + filter.getValue());
              break;
            case CN:
              criteria.andLike(filter.getPropertyName(), "%" + filter.getValue() + "%");
              break;
            case NC:
              criteria.andNotLike(filter.getPropertyName(), "%" + filter.getValue() + "%");
              break;
            case NU:
              criteria.andIsNull(filter.getPropertyName());
              break;
            case NN:
              criteria.andIsNotNull(filter.getPropertyName());
              break;
            case IN:
              List<Object> in = new ArrayList();
              for (Object val : (List) filter.getValue()) {
                in.add(stringTo(val, fieldClazz));
              }
              criteria.andIn(filter.getPropertyName(), in);
              break;
            case NI:
              List<Object> nin = new ArrayList();
              for (Object val : (List) filter.getValue()) {
                nin.add(stringTo(val, fieldClazz));
              }
              criteria.andNotIn(filter.getPropertyName(), nin);
              break;
          }
        }
      }
    }

    return example;
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
}
