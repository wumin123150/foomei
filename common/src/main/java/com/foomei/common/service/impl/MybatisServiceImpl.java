package com.foomei.common.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.foomei.common.dao.MybatisDao;
import com.foomei.common.persistence.DynamicExample;
import com.foomei.common.persistence.JqGridFilter;
import com.foomei.common.persistence.SearchFilter;
import com.foomei.common.reflect.ClassUtil;
import com.foomei.common.service.MybatisService;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;

public abstract class MybatisServiceImpl<DAO extends MybatisDao<T>, T, ID extends Serializable> implements MybatisService<DAO, T, ID> {

  protected static final Logger logger = LoggerFactory.getLogger(MybatisServiceImpl.class);

  protected DAO dao;
  protected Class<T> entityClazz;

  public MybatisServiceImpl() {
    this.entityClazz = ClassUtil.getClassGenricType(getClass(), 1);
  }

  public T get(ID id) {
    return dao.selectByPrimaryKey(id);
  }

  public List<T> getAll() {
    return dao.selectByExample(new Example(entityClazz));
  }

  @Transactional(readOnly = false)
  public T insert(T entity) {
    dao.insert(entity);
    logger.info("insert entity: {}", entity);
    return entity;
  }

  @Transactional(readOnly = false)
  public T update(T entity) {
    dao.updateByPrimaryKey(entity);
    logger.info("update entity: {}", entity);
    return entity;
  }

  @Transactional(readOnly = false)
  public void delete(ID id) {
    dao.deleteByPrimaryKey(id);
    logger.info("delete entity {}, id is {}", entityClazz.getName(), id);
  }

  public List<T> getList(SearchFilter searchFilter, Sort sort) {
    Example example = DynamicExample.bySearchFilter(searchFilter, sort, entityClazz);
    return dao.selectByExample(example);
  }

  public List<T> getList(Map<String, Object> searchParams) {
    SearchFilter searchFilter = SearchFilter.parse(searchParams);
    return getList(searchFilter, null);
  }

  public Page<T> getPage(SearchFilter searchFilter, Pageable page) {
    Example example = DynamicExample.bySearchFilter(searchFilter, entityClazz);
    com.github.pagehelper.Page<T> pager = PageHelper.startPage(page.getPageNumber(), page.getPageSize(), true);
    if (page.getSort() != null) {
      example.setOrderByClause(page.getSort().toString());
    }
    dao.selectByExample(example);
    return new PageImpl<>(pager.getResult(), page, pager.getTotal());
  }

  public Page<T> getPage(Map<String, Object> searchParams, Pageable page) {
    SearchFilter searchFilter = SearchFilter.parse(searchParams);
    return getPage(searchFilter, page);
  }

  public Page<T> getPage(JqGridFilter jqGridFilter, Pageable page) {
    SearchFilter searchFilter = SearchFilter.parse(jqGridFilter);
    return getPage(searchFilter, page);
  }

  public Long count(SearchFilter searchFilter) {
    Example example = DynamicExample.bySearchFilter(searchFilter, entityClazz);
    return Long.valueOf(dao.selectCountByExample(example));
  }

  public Long count(Map<String, Object> searchParams) {
    SearchFilter searchFilter = SearchFilter.parse(searchParams);
    return count(searchFilter);
  }

  public void setDao(DAO dao) {
    this.dao = dao;
  }

}
