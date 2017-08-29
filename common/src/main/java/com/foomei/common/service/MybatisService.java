package com.foomei.common.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.foomei.common.base.annotation.AutowiredDao;
import com.foomei.common.dao.MybatisDao;
import com.foomei.common.persistence.JqGridFilter;
import com.foomei.common.persistence.SearchFilter;

public interface MybatisService<DAO extends MybatisDao<T>, T, ID extends Serializable> {

	public T get(ID id);

	public List<T> getAll();

	public T insert(T entity);
	
	public T update(T entity);

	public void delete(ID id);
	
	public List<T> getList(SearchFilter searchFilter, Sort sort);
    
    public List<T> getList(Map<String, Object> searchParams);
    
    public Page<T> getPage(SearchFilter searchFilter, Pageable page);

    public Page<T> getPage(Map<String, Object> searchParams, Pageable page);
    
    public Page<T> getPage(JqGridFilter jqGridFilter, Pageable page);
    
    public Long count(SearchFilter searchFilter);
    
    public Long count(Map<String, Object> searchParams);
	
	@AutowiredDao
    public void setDao(DAO dao);
	
}
