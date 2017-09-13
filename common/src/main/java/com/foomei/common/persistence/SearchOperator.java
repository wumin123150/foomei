package com.foomei.common.persistence;


public enum SearchOperator {

  /**
   * 属性比较类型.
   * 'equal', 'not equal', 'less', 'less or equal', 'greater', 'greater or equal', 'begins with', 'does not begin with', 'is in', 'is not in', 'ends with', 'does not end with', 'contains', 'does not contain'
   */
  EQ("等于", "EQ"), NE("不等于", "NE"), LT("小于", "LT"), LTE("小于等于", "LE"), GT("大于", "GT"), GTE("大于等于", "GE")
  , PRE_LIKE("前缀模糊匹配", "BW"), PRE_NOT_LIKE("前缀模糊不匹配", "BN"), IN("包含", "IN"), NOT_IN("不包含", "NI")
  , SFX_LIKE("后缀模糊匹配", "EW"), SFX_NOT_LIKE("后缀模糊不匹配", "EN"), LIKE("模糊匹配", "CN"), NOT_LIKE("不匹配", "NC")
  , IS_NULL("空", "NU"), IS_NOT_NULL("非空", "NN"), CUSTOM("自定义", null);

  private final String name;
  private final String value;

  SearchOperator(final String name, String value) {
    this.name = name;
    this.value = value;
  }

}
