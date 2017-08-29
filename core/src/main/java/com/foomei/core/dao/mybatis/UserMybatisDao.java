package com.foomei.core.dao.mybatis;

import java.util.List;

import com.foomei.common.base.annotation.MybatisRepository;
import com.foomei.core.entity.BaseUser;

/**
 * 通过@MapperScannerConfigurer扫描目录中的所有接口, 动态在Spring Context中生成实现.
 * 方法名称必须与Mapper.xml中保持一致.
 * 
 * @author walker
 */
@MybatisRepository
public interface UserMybatisDao {

	List<BaseUser> findByRole(List<String> roleCodes);

}
