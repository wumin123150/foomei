package com.foomei.common.dao;

import org.springframework.data.repository.NoRepositoryBean;

import com.github.abel533.mapper.Mapper;

@NoRepositoryBean
public interface MybatisDao<T> extends Mapper<T> {

}
