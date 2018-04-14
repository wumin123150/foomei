package com.foomei.common.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface MybatisDao<T> extends BaseMapper<T> {

}

/**
 * 使用tkmybatis
import tk.mybatis.mapper.common.Mapper;

@NoRepositoryBean
public interface MybatisDao<T> extends Mapper<T> {

}
*/
