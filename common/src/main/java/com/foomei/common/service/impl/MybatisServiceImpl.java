package com.foomei.common.service.impl;

import com.foomei.common.dao.MybatisDao;
import com.foomei.common.persistence.DynamicExample;
import com.foomei.common.persistence.search.SearchRequest;
import com.foomei.common.reflect.ClassUtil;
import com.foomei.common.service.MybatisService;
import com.github.abel533.entity.Example;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

public abstract class MybatisServiceImpl<T, ID extends Serializable> implements MybatisService<T, ID> {

  protected static final Logger logger = LoggerFactory.getLogger(MybatisServiceImpl.class);

  protected MybatisDao<T> dao;
  protected Class<T> entityClazz;

  public MybatisServiceImpl() {
    this.entityClazz = ClassUtil.getClassGenricType(getClass(), 0);
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

  public List<T> getList(SearchRequest searchRequest) {
    Example example = DynamicExample.bySearchRequest(searchRequest, entityClazz);
    return dao.selectByExample(example);
  }

  public Page<T> getPage(SearchRequest searchRequest) {
    Example example = DynamicExample.bySearchRequest(searchRequest, entityClazz);
    Pageable page = searchRequest.getPage();
    com.github.pagehelper.Page<T> pager = PageHelper.startPage(page.getPageNumber(), page.getPageSize(), true);
    dao.selectByExample(example);
    return new PageImpl<>(pager.getResult(), page, pager.getTotal());
  }

  public Long count(SearchRequest searchRequest) {
    Example example = DynamicExample.bySearchRequest(searchRequest, entityClazz);
    return Long.valueOf(dao.selectCountByExample(example));
  }

  @Autowired
  public void setMybatisDao(MybatisDao<T> mybatisDao) {
    this.dao = mybatisDao;
  }

}
