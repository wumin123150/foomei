package com.foomei.common.dao;

import org.springframework.data.repository.NoRepositoryBean;
import tk.mybatis.mapper.common.Mapper;

@NoRepositoryBean
public interface MybatisDao<T> extends Mapper<T> {

}
