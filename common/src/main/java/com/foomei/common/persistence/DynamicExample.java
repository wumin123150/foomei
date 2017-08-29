package com.foomei.common.persistence;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import com.foomei.common.collection.CollectionUtil;
import com.foomei.common.persistence.SearchFilter.Filter;
import com.foomei.common.reflect.ClassUtil;
import com.foomei.common.time.DateFormatUtil;
import com.github.abel533.entity.Example;
import com.github.abel533.entity.Example.Criteria;

public class DynamicExample {
    
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Example bySearchFilter(final SearchFilter searchFilter, final Sort sort, final Class<T> entityClazz) {
        Example example = bySearchFilter(searchFilter, entityClazz);
        if(sort != null) {
            StringBuilder builder = new StringBuilder();
            for (Iterator iterator = sort.iterator(); iterator.hasNext();) {
                Order order = (Order) iterator.next();
                builder.append(",").append(order.getProperty()).append(" ").append(order.getDirection().name());
            }
            if(builder.length() > 0) {
                example.setOrderByClause(builder.substring(1));
            }
        }
        return example;
    }
    
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static <T> Example bySearchFilter(final SearchFilter searchFilter, final Class<T> entityClazz) {
		Example example = new Example(entityClazz);
		
		Criteria criteria = null;
		if(CollectionUtil.isNotEmpty(searchFilter.filters)) {
			for (Filter filter : searchFilter.filters) {
			    if(searchFilter.or || criteria == null) {
			        criteria = example.or();
			    }
				
				// nested path translate, 如Task的名为"user.name"的filedName, 转换为Task.user.name属性
				Field field = ClassUtil.getAccessibleField(entityClazz, filter.fieldName);
				Class fieldClazz = field != null ? field.getClass() : String.class;
				
				Object value = null;

				// logic operator
				switch (filter.operator) {
				case EQ:
					value = stringTo(filter.value, fieldClazz);
					criteria.andEqualTo(filter.fieldName, value);
					break;
				case NE:
					value = stringTo(filter.value, fieldClazz);
					criteria.andNotEqualTo(filter.fieldName, value);
					break;
				case LT:
					value = stringTo(filter.value, fieldClazz);
					criteria.andLessThan(filter.fieldName, value);
					break;
				case LE:
					value = stringTo(filter.value, fieldClazz);
					criteria.andLessThanOrEqualTo(filter.fieldName, value);
					break;
				case GT:
					value = stringTo(filter.value, fieldClazz);
					criteria.andGreaterThan(filter.fieldName, value);
					break;
				case GE:
					value = stringTo(filter.value, fieldClazz);
					criteria.andGreaterThanOrEqualTo(filter.fieldName, value);
					break;
				case BW:
					criteria.andLike(filter.fieldName, filter.value + "%");
					break;
				case BN:
					criteria.andNotLike(filter.fieldName, filter.value + "%");
					break;
				case EW:
					criteria.andLike(filter.fieldName, "%" + filter.value);
					break;
				case EN:
					criteria.andNotLike(filter.fieldName, "%" + filter.value);
					break;
				case CN:
					criteria.andLike(filter.fieldName, "%" + filter.value + "%");
					break;
				case NC:
					criteria.andNotLike(filter.fieldName, "%" + filter.value + "%");
					break;
				case NU:
					criteria.andIsNull(filter.fieldName);
					break;
				case NN:
					criteria.andIsNotNull(filter.fieldName);
					break;
				case IN:
					List<Object> in = new ArrayList();
					for (Object val : (List)filter.value) {
                        in.add(stringTo(val, fieldClazz));
                    }
					criteria.andIn(filter.fieldName, in);
					break;
				case NI:
					List<Object> nin = new ArrayList();
					for (Object val : (List)filter.value) {
                        nin.add(stringTo(val, fieldClazz));
                    }
					criteria.andNotIn(filter.fieldName, nin);
					break;
				}
			}
		}

		return example;
	}
	
	private static Object stringTo(Object value, final Class clazz) {
        if(value instanceof String) {
            if(Boolean.class.isAssignableFrom(clazz)) {
                return Boolean.valueOf((String)value);
            }
            if(Short.class.isAssignableFrom(clazz)) {
                return Short.valueOf((String)value);
            }
            if(Integer.class.isAssignableFrom(clazz)) {
                return Integer.valueOf((String)value);
            }
            if(Long.class.isAssignableFrom(clazz)) {
                return Long.valueOf((String)value);
            }
            if(Float.class.isAssignableFrom(clazz)) {
                return Float.valueOf((String)value);
            }
            if(Double.class.isAssignableFrom(clazz)) {
                return Double.valueOf((String)value);
            }
            if(BigDecimal.class.isAssignableFrom(clazz)) {
                return new BigDecimal((String)value);
            }
            if(Date.class.isAssignableFrom(clazz)) {
                return DateFormatUtil.parseShortDate((String)value);
            }
        }
        return value;
    }
}
