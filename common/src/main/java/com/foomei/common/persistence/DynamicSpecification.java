package com.foomei.common.persistence;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import com.foomei.common.collection.CollectionUtil;
import com.foomei.common.persistence.SearchFilter.Filter;
import com.foomei.common.time.DateFormatUtil;
import com.google.common.collect.Lists;

public class DynamicSpecification {
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static <T> Specification<T> bySearchFilter(final SearchFilter searchFilter, final Class<T> entityClazz) {
		return new Specification<T>() {
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				if (CollectionUtil.isNotEmpty(searchFilter.filters)) {

					List<Predicate> predicates = Lists.newArrayList();
					for (Filter filter : searchFilter.filters) {
						// nested path translate, 如Task的名为"user.name"的filedName, 转换为Task.user.name属性
						String[] names = StringUtils.split(filter.fieldName, ".");
						Path expression = root.get(names[0]);
						for (int i = 1; i < names.length; i++) {
							expression = expression.get(names[i]);
						}
						
						Object value = null;

						// logic operator
						switch (filter.operator) {
						case EQ:
							value = stringTo(filter.value, expression.getJavaType());
							predicates.add(builder.equal(expression, value));
							break;
						case NE:
							value = stringTo(filter.value, expression.getJavaType());
							predicates.add(builder.notEqual(expression, value));
							break;
						case LT:
							value = stringTo(filter.value, expression.getJavaType());
							predicates.add(builder.lessThan(expression, (Comparable) value));
							break;
						case LE:
							value = stringTo(filter.value, expression.getJavaType());
							predicates.add(builder.lessThanOrEqualTo(expression, (Comparable) value));
							break;
						case GT:
							value = stringTo(filter.value, expression.getJavaType());
							predicates.add(builder.greaterThan(expression, (Comparable) value));
							break;
						case GE:
							value = stringTo(filter.value, expression.getJavaType());
							predicates.add(builder.greaterThanOrEqualTo(expression, (Comparable) value));
							break;
						case BW:
							predicates.add(builder.like(expression, filter.value + "%"));
							break;
						case BN:
							predicates.add(builder.notLike(expression, filter.value + "%"));
							break;
						case EW:
							predicates.add(builder.like(expression, "%" + filter.value));
							break;
						case EN:
							predicates.add(builder.notLike(expression, "%" + filter.value));
							break;
						case CN:
							predicates.add(builder.like(expression, "%" + filter.value + "%"));
							break;
						case NC:
							predicates.add(builder.notLike(expression, "%" + filter.value + "%"));
							break;
						case NU:
							predicates.add(builder.isNull(expression));
							break;
						case NN:
							predicates.add(builder.isNotNull(expression));
							break;
						case IN:
							In in = builder.in(expression);
							for (Object val : (List)filter.value) {
								in.value(stringTo(val, expression.getJavaType()));
							}
							predicates.add(in);
							break;
						case NI:
							In nin = builder.in(expression);
							for (Object val : (List)filter.value) {
								nin.value(stringTo(val, expression.getJavaType()));
							}
							predicates.add(builder.not(nin));
							break;
						default: break;
						}
					}

					// 将所有条件用 and 联合起来
					if (!predicates.isEmpty()) {
						if(searchFilter.or) {
						    return builder.or(predicates.toArray(new Predicate[predicates.size()]));
						} else {
						    return builder.and(predicates.toArray(new Predicate[predicates.size()]));
						}
					}
				}

				return builder.conjunction();
			}
		};
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
            if(String.class.isAssignableFrom(clazz)) {
                return StringUtils.trim((String)value);
            }
        }
        return value;
    }
}
