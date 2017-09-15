package com.foomei.common.service.impl;

import com.foomei.common.dao.JpaDao;
import com.foomei.common.entity.Deletable;
import com.foomei.common.persistence.DynamicSpecification;
import com.foomei.common.persistence.search.SearchRequest;
import com.foomei.common.reflect.ClassUtil;
import com.foomei.common.service.JpaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

public abstract class JpaServiceImpl<T, ID extends Serializable> implements JpaService<T, ID> {

  protected static final Logger logger = LoggerFactory.getLogger(JpaServiceImpl.class);

  protected JpaDao<T, ID> dao;
  protected Class<T> entityClazz;

  public JpaServiceImpl() {
    this.entityClazz = ClassUtil.getClassGenricType(getClass(), 0);
  }

  public T get(ID id) {
    return dao.findOne(id);
  }

  public List<T> getAll() {
    return dao.findAll();
  }

  @Transactional(readOnly = false)
  public T save(T entity) {
    T t = dao.save(entity);
    logger.info("save entity: {}", entity);
    return t;
  }

  @Transactional(readOnly = false)
  public T saveAndFlush(T entity) {
    T t = dao.save(entity);
    dao.flush();
    return t;
  }

  @Transactional(readOnly = false)
  public void delete(ID id) {
    T t = get(id);
    delete(t);
  }

  @Transactional(readOnly = false)
  public void delete(final T entity) {
    if (entity == null) {
      return;
    }
    if (entity instanceof Deletable) {
      ((Deletable) entity).markDeleted();
      save(entity);
    } else {
      dao.delete(entity);
      logger.info("delete entity: {}", entity);
    }
  }

  public List<T> getList(SearchRequest searchRequest) {
    Specification<T> spec = DynamicSpecification.bySearchRequest(searchRequest, entityClazz);
    return dao.findAll(spec, searchRequest.getSort());
  }

  public Page<T> getPage(SearchRequest searchRequest) {
    Specification<T> spec = DynamicSpecification.bySearchRequest(searchRequest, entityClazz);
    return dao.findAll(spec, searchRequest.getPage());
  }

  public Long count(SearchRequest searchRequest) {
    Specification<T> spec = DynamicSpecification.bySearchRequest(searchRequest, entityClazz);
    return dao.count(spec);
  }

  @Autowired
  public void setJpaDao(JpaDao<T, ID> jpaDao) {
    this.dao = jpaDao;
  }

}
