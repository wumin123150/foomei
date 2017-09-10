package com.foomei.common.service.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.foomei.common.dao.JpaDao;
import com.foomei.common.persistence.DynamicSpecification;
import com.foomei.common.persistence.JqGridFilter;
import com.foomei.common.persistence.SearchFilter;
import com.foomei.common.reflect.ClassUtil;
import com.foomei.common.service.JpaService;

public abstract class JpaServiceImpl<DAO extends JpaDao<T, ID>, T, ID extends Serializable> implements JpaService<DAO, T, ID> {

  protected static final Logger logger = LoggerFactory.getLogger(JpaServiceImpl.class);

  protected DAO dao;
  protected Class<T> entityClazz;

  public JpaServiceImpl() {
    this.entityClazz = ClassUtil.getClassGenricType(getClass(), 1);
  }

  public T get(ID id) {
    return dao.findOne(id);
  }

  public List<T> getAll() {
    return dao.findAll();
  }

  public T save(T entity) {
    T t = dao.save(entity);
    logger.info("save entity: {}", entity);
    return t;
  }

  public void delete(ID id) {
    dao.delete(id);
    logger.info("delete entity {}, id is {}", entityClazz.getName(), id);
  }

  public List<T> getList(SearchFilter searchFilter, Sort sort) {
    Specification<T> spec = DynamicSpecification.bySearchFilter(searchFilter, entityClazz);
    return dao.findAll(spec, sort);
  }

  public List<T> getList(Map<String, Object> searchParams) {
    SearchFilter searchFilter = SearchFilter.parse(searchParams);
    return getList(searchFilter, null);
  }

  public Page<T> getPage(SearchFilter searchFilter, Pageable page) {
    Specification<T> spec = DynamicSpecification.bySearchFilter(searchFilter, entityClazz);
    return dao.findAll(spec, page);
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
    Specification<T> spec = DynamicSpecification.bySearchFilter(searchFilter, entityClazz);
    return dao.count(spec);
  }

  public Long count(Map<String, Object> searchParams) {
    SearchFilter searchFilter = SearchFilter.parse(searchParams);
    return count(searchFilter);
  }

  public void setDao(DAO dao) {
    this.dao = dao;
  }
}
