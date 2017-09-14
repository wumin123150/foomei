package com.foomei.common.service;

import com.foomei.common.persistence.search.SearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

public interface JpaService<T, ID extends Serializable> {

	public T get(ID id);

	public List<T> getAll();

	@Transactional(readOnly = false)
	public T save(T entity);

	@Transactional(readOnly = false)
	public void delete(ID id);
	
	public List<T> getList(SearchRequest searchRequest);
	
	public Page<T> getPage(SearchRequest searchRequest);
	
	public Long count(SearchRequest searchRequest);
	
}
