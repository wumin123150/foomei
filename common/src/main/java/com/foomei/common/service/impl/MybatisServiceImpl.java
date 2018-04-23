package com.foomei.common.service.impl;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.foomei.common.dao.MybatisDao;
import com.foomei.common.entity.CreateRecord;
import com.foomei.common.entity.DeleteRecord;
import com.foomei.common.entity.UpdateRecord;
import com.foomei.common.persistence.DynamicWrapper;
import com.foomei.common.persistence.search.SearchRequest;
import com.foomei.common.reflect.ClassUtil;
import com.foomei.common.service.MybatisService;
import com.foomei.common.web.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public abstract class MybatisServiceImpl<M extends BaseMapper<T>, T, ID extends Serializable> extends ServiceImpl<M, T> implements MybatisService<T, ID> {

  protected static final Logger LOGGER = LoggerFactory.getLogger(MybatisServiceImpl.class);

  protected MybatisDao<T> dao;
  protected Class<T> entityClazz;

  public MybatisServiceImpl() {
    this.entityClazz = ClassUtil.getClassGenricType(getClass(), 1);
  }

  public T get(ID id) {
    return super.selectById(id);
  }

  public List<T> getAll() {
    return super.selectList(new EntityWrapper<T>());
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public boolean save(T entity) {
    if(entity instanceof CreateRecord && !((CreateRecord) entity).isCreated()) {
      ((CreateRecord) entity).setCreateTime(new Date());
      ((CreateRecord) entity).setCreator(ThreadContext.getUserId());
      if (entity instanceof DeleteRecord) {
        ((DeleteRecord) entity).setDelFlag(false);
      }
    }
    if (entity instanceof UpdateRecord) {
      ((UpdateRecord) entity).setUpdateTime(new Date());
      ((UpdateRecord) entity).setUpdator(ThreadContext.getUserId());
    }
    boolean result = super.insertOrUpdate(entity);
    LOGGER.info("insert entity: {}", entity);
    return result;
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public boolean insert(T entity) {
    if(entity instanceof CreateRecord && !((CreateRecord) entity).isCreated()) {
      ((CreateRecord) entity).setCreateTime(new Date());
      ((CreateRecord) entity).setCreator(ThreadContext.getUserId());
      if (entity instanceof DeleteRecord) {
        ((DeleteRecord) entity).setDelFlag(false);
      }
    }
    boolean result = super.insert(entity);
    LOGGER.info("insert entity: {}", entity);
    return result;
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public boolean update(T entity) {
    if (entity instanceof UpdateRecord) {
      ((UpdateRecord) entity).setUpdateTime(new Date());
      ((UpdateRecord) entity).setUpdator(ThreadContext.getUserId());
    }
    boolean result = super.updateById(entity);
    LOGGER.info("update entity: {}", entity);
    return result;
  }

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void delete(ID id) {
    T entity = get(id);
    if (entity instanceof DeleteRecord) {
      ((DeleteRecord) entity).markDeleted();
      update(entity);
    } else {
      dao.deleteById(id);
      LOGGER.info("delete entity: {}", entity);
    }
  }

  public List<T> getList(SearchRequest searchRequest) {
    EntityWrapper<T> wrapper = DynamicWrapper.bySearchRequest(searchRequest, entityClazz);
    return super.selectList(wrapper);
  }

  public Page<T> getPage(SearchRequest searchRequest) {
    EntityWrapper<T> wrapper = DynamicWrapper.bySearchRequest(searchRequest, entityClazz);
    Pageable page = searchRequest.getPage();
    return getPage(wrapper, page);
  }

  public Long count(SearchRequest searchRequest) {
    EntityWrapper<T> wrapper = DynamicWrapper.bySearchRequest(searchRequest, entityClazz);
    int count = super.selectCount(wrapper);
    return Long.valueOf(count);
  }

  public Page<T> getPage(EntityWrapper<T> wrapper, Pageable page) {
    com.baomidou.mybatisplus.plugins.Page<T> pager = new com.baomidou.mybatisplus.plugins.Page<T>(page.getPageNumber(), page.getPageSize());
    pager = super.selectPage(pager, wrapper);
    return new PageImpl<>(pager.getRecords(), page, pager.getTotal());
  }

  @Autowired
  public void setMybatisDao(MybatisDao<T> mybatisDao) {
    this.dao = mybatisDao;
  }

}

/**
 * 使用tkmybatis
import com.foomei.common.dao.MybatisDao;
import com.foomei.common.entity.CreateRecord;
import com.foomei.common.entity.DeleteRecord;
import com.foomei.common.entity.UpdateRecord;
import com.foomei.common.persistence.DynamicExample;
import com.foomei.common.persistence.search.SearchRequest;
import com.foomei.common.reflect.ClassUtil;
import com.foomei.common.service.MybatisService;
import com.foomei.common.web.ThreadContext;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public abstract class MybatisServiceImpl<T, ID extends Serializable> implements MybatisService<T, ID> {

  protected static final Logger LOGGER = LoggerFactory.getLogger(MybatisServiceImpl.class);

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
    if(entity instanceof CreateRecord && !((CreateRecord) entity).isCreated()) {
      ((CreateRecord) entity).setCreateTime(new Date());
      ((CreateRecord) entity).setCreator(ThreadContext.getUserId());
      if (entity instanceof DeleteRecord) {
        ((DeleteRecord) entity).setDelFlag(false);
      }
    }
    dao.insert(entity);
    LOGGER.info("insert entity: {}", entity);
    return entity;
  }

  @Transactional(readOnly = false)
  public T insertSelective(T entity) {
    if(entity instanceof CreateRecord && !((CreateRecord) entity).isCreated()) {
      ((CreateRecord) entity).setCreateTime(new Date());
      ((CreateRecord) entity).setCreator(ThreadContext.getUserId());
      if (entity instanceof DeleteRecord) {
        ((DeleteRecord) entity).setDelFlag(false);
      }
    }
    dao.insertSelective(entity);
    LOGGER.info("insert entity: {}", entity);
    return entity;
  }

  @Transactional(readOnly = false)
  public T update(T entity) {
    if (entity instanceof UpdateRecord) {
      ((UpdateRecord) entity).setUpdateTime(new Date());
      ((UpdateRecord) entity).setUpdator(ThreadContext.getUserId());
    }
    dao.updateByPrimaryKey(entity);
    LOGGER.info("update entity: {}", entity);
    return entity;
  }

  @Transactional(readOnly = false)
  public T updateSelective(T entity) {
    if (entity instanceof UpdateRecord) {
      ((UpdateRecord) entity).setUpdateTime(new Date());
      ((UpdateRecord) entity).setUpdator(ThreadContext.getUserId());
    }
    dao.updateByPrimaryKeySelective(entity);
    LOGGER.info("update entity: {}", entity);
    return entity;
  }

  @Transactional(readOnly = false)
  public void delete(ID id) {
    T t = get(id);
    delete(t);
  }

  @Transactional(readOnly = false)
  public void delete(T entity) {
    if (entity == null) {
      return;
    }
    if (entity instanceof DeleteRecord) {
      ((DeleteRecord) entity).markDeleted();
      update(entity);
    } else {
      dao.delete(entity);
      LOGGER.info("delete entity: {}", entity);
    }
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
*/
