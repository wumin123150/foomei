package com.foomei.common.service;

import com.foomei.common.persistence.search.SearchRequest;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

public interface MybatisService<T, ID extends Serializable> {

  public T get(ID id);

  public List<T> getAll();

  public T insert(T entity);

  public T update(T entity);

  public void delete(ID id);

  public List<T> getList(SearchRequest searchRequest);

  public Page<T> getPage(SearchRequest searchRequest);

  public Long count(SearchRequest searchRequest);

}
