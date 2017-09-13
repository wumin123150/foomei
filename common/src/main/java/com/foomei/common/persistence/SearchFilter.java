package com.foomei.common.persistence;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.foomei.common.collection.ListUtil;
import com.google.common.collect.Lists;

/**
 * 与具体ORM实现无关的属性过滤条件封装类.
 * <p>
 * PropertyFilter主要记录页面中简单的搜索过滤条件,比Hibernate的Criterion要简单.
 *
 * @author walker
 */
public class SearchFilter {

  /**
   * 属性比较类型.
   * 'equal', 'not equal', 'less', 'less or equal', 'greater', 'greater or equal', 'begins with', 'does not begin with', 'is in', 'is not in', 'ends with', 'does not end with', 'contains', 'does not contain'
   */
  public enum Operator {
    EQ, NE, LT, LE, GT, GE, BW, BN, IN, NI, EW, EN, CN, NC, NU, NN,
  }

  protected boolean or;
  protected List<Filter> filters = Lists.newArrayList();

  public SearchFilter() {

  }

  public SearchFilter or() {
    or = true;
    return this;
  }

  public static SearchFilter parse(Map<String, Object> searchParams) {
    return new SearchFilter().addFilters(searchParams);
  }

  public static SearchFilter parse(JqGridFilter jqGridFilter) {
    return new SearchFilter().addFilters(jqGridFilter);
  }

  public SearchFilter addFilters(Map<String, Object> searchParams) {
    for (Entry<String, Object> entry : searchParams.entrySet()) {
      String key = entry.getKey();
      Object value = entry.getValue();

      // 拆分operator与fieldAttribute
      String[] names = StringUtils.split(key, "_");
      if (names.length != 2) {
        throw new IllegalArgumentException(key + " is not a valid search filter name");
      }
      String filedName = names[1];
      Operator operator = Operator.valueOf(names[0].toUpperCase());

      if (operator == Operator.EQ || operator == Operator.NE || operator == Operator.LT
        || operator == Operator.LE || operator == Operator.GT || operator == Operator.GE
        || operator == Operator.BW || operator == Operator.BN || operator == Operator.EW
        || operator == Operator.EN || operator == Operator.CN || operator == Operator.NC
        || operator == Operator.IN || operator == Operator.NI) {
        if (value == null || (value instanceof String && StringUtils.isEmpty((String) value))) {
          continue;
        }
      }

      if (operator == Operator.IN || operator == Operator.NI) {
        if (value instanceof Object[]) {
          value = Arrays.asList((Object[]) value);
        }

        value = ListUtil.newArrayList(value);
      }

      // 创建filter
      Filter filter = new Filter(filedName, operator, value);
      filters.add(filter);
    }

    return this;
  }

  public SearchFilter addFilters(JqGridFilter jqGridFilter) {
    if (jqGridFilter != null) {
      or = StringUtils.equalsIgnoreCase(jqGridFilter.getGroupOp(), "or");

      for (JqGridRule jqGridRule : jqGridFilter.getRules()) {
        String field = jqGridRule.getField();
        Object value = jqGridRule.getData();
        Operator operator = Operator.valueOf(jqGridRule.getOp().toUpperCase());

        if (operator == Operator.EQ || operator == Operator.NE || operator == Operator.LT
          || operator == Operator.LE || operator == Operator.GT || operator == Operator.GE
          || operator == Operator.BW || operator == Operator.BN || operator == Operator.EW
          || operator == Operator.EN || operator == Operator.CN || operator == Operator.NC
          || operator == Operator.IN || operator == Operator.NI) {
          if (value == null || (value instanceof String && StringUtils.isEmpty((String) value))) {
            continue;
          }
        }

        if (operator == Operator.IN || operator == Operator.NI) {
          value = Arrays.asList(StringUtils.split((String) value, ", "));
        }

        // 创建filter
        Filter filter = new Filter(field, operator, value);
        filters.add(filter);
      }
    }

    return this;
  }

  public SearchFilter addEqualTo(String fieldName, Object value) {
    filters.add(equalTo(fieldName, value));
    return this;
  }

  public SearchFilter addNotEqualTo(String fieldName, Object value) {
    filters.add(notEqualTo(fieldName, value));
    return this;
  }

  public SearchFilter addLessThan(String fieldName, Object value) {
    filters.add(lessThan(fieldName, value));
    return this;
  }

  public SearchFilter addLessThanOrEqualTo(String fieldName, Object value) {
    filters.add(lessThanOrEqualTo(fieldName, value));
    return this;
  }

  public SearchFilter addGreaterThan(String fieldName, Object value) {
    filters.add(greaterThan(fieldName, value));
    return this;
  }

  public SearchFilter addGreaterOrEqualTo(String fieldName, Object value) {
    filters.add(greaterOrEqualTo(fieldName, value));
    return this;
  }

  public SearchFilter addStartWith(String fieldName, Object value) {
    filters.add(startWith(fieldName, value));
    return this;
  }

  public SearchFilter addNotStartWith(String fieldName, Object value) {
    filters.add(notStartWith(fieldName, value));
    return this;
  }

  public SearchFilter addEndWith(String fieldName, Object value) {
    filters.add(endWith(fieldName, value));
    return this;
  }

  public SearchFilter addNotEndWith(String fieldName, Object value) {
    filters.add(notEndWith(fieldName, value));
    return this;
  }

  public SearchFilter addLike(String fieldName, Object value) {
    filters.add(like(fieldName, value));
    return this;
  }

  public SearchFilter addNotLike(String fieldName, Object value) {
    filters.add(notLike(fieldName, value));
    return this;
  }

  public SearchFilter addIsNull(String fieldName) {
    filters.add(isNull(fieldName));
    return this;
  }

  public SearchFilter addNotNull(String fieldName) {
    filters.add(notNull(fieldName));
    return this;
  }

  public SearchFilter addIn(String fieldName, Collection<Object> values) {
    filters.add(in(fieldName, values));
    return this;
  }

  public SearchFilter addNotIn(String fieldName, Collection<Object> values) {
    filters.add(notIn(fieldName, values));
    return this;
  }

  public static Filter equalTo(String fieldName, Object value) {
    return new Filter(fieldName, Operator.EQ, value);
  }

  public static Filter notEqualTo(String fieldName, Object value) {
    return new Filter(fieldName, Operator.NE, value);
  }

  public static Filter lessThan(String fieldName, Object value) {
    return new Filter(fieldName, Operator.LT, value);
  }

  public static Filter lessThanOrEqualTo(String fieldName, Object value) {
    return new Filter(fieldName, Operator.LE, value);
  }

  public static Filter greaterThan(String fieldName, Object value) {
    return new Filter(fieldName, Operator.GT, value);
  }

  public static Filter greaterOrEqualTo(String fieldName, Object value) {
    return new Filter(fieldName, Operator.GE, value);
  }

  public static Filter startWith(String fieldName, Object value) {
    return new Filter(fieldName, Operator.BW, value);
  }

  public static Filter notStartWith(String fieldName, Object value) {
    return new Filter(fieldName, Operator.BN, value);
  }

  public static Filter endWith(String fieldName, Object value) {
    return new Filter(fieldName, Operator.EW, value);
  }

  public static Filter notEndWith(String fieldName, Object value) {
    return new Filter(fieldName, Operator.EN, value);
  }

  public static Filter like(String fieldName, Object value) {
    return new Filter(fieldName, Operator.CN, value);
  }

  public static Filter notLike(String fieldName, Object value) {
    return new Filter(fieldName, Operator.NC, value);
  }

  public static Filter isNull(String fieldName) {
    return new Filter(fieldName, Operator.NU, null);
  }

  public static Filter notNull(String fieldName) {
    return new Filter(fieldName, Operator.NN, null);
  }

  public static Filter in(String fieldName, Collection<Object> values) {
    return new Filter(fieldName, Operator.IN, values);
  }

  public static Filter notIn(String fieldName, Collection<Object> values) {
    return new Filter(fieldName, Operator.NI, values);
  }

  public static class Filter {
    protected String fieldName;
    protected Object value;
    protected Operator operator;

    public Filter(String fieldName, Operator operator, Object value) {
      this.fieldName = fieldName;
      this.value = value != null ? value : "";
      this.operator = operator;
    }

  }
}
