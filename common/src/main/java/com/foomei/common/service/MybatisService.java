package com.foomei.common.service;

import com.foomei.common.persistence.search.SearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

public interface MybatisService<T, ID extends Serializable> {

  public T get(ID id);

  public List<T> getAll();

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public boolean save(T entity);

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public boolean insert(T entity);

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public boolean update(T entity);

  @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
  public void delete(ID id);

  public List<T> getList(SearchRequest searchRequest);

  public Page<T> getPage(SearchRequest searchRequest);

  public Long count(SearchRequest searchRequest);

}
