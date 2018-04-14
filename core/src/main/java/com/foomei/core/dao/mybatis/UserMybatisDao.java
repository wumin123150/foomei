package com.foomei.core.dao.mybatis;

import com.foomei.common.base.annotation.MybatisRepository;
import com.foomei.common.dao.MybatisDao;
import com.foomei.core.entity.BaseUser;

import java.util.List;

/**
 * 通过@MapperScannerConfigurer扫描目录中的所有接口, 动态在Spring Context中生成实现.
 * 方法名称必须与Mapper.xml中保持一致.
 * 
 * @author walker
 */
@MybatisRepository
public interface UserMybatisDao extends MybatisDao<BaseUser>  {

	List<BaseUser> findByRole(List<String> roleCodes);

}
