package com.foomei.core.web;

import com.foomei.common.mapper.BeanMapper;
import com.foomei.common.mapper.FieldsMapper;
import com.foomei.core.entity.Role;
import com.foomei.core.entity.User;
import com.foomei.core.entity.UserGroup;
import com.foomei.core.vo.RoleVo;
import com.foomei.core.vo.UserGroupVo;
import com.foomei.core.vo.UserVo;

public class BeanMapperRegister {

  public void init() {
    BeanMapper.registerClassMap(User.class, UserVo.class, new FieldsMapper<User, UserVo>() {
      @Override
      public void map(User user, UserVo userVo) {
        userVo.setRoles(BeanMapper.mapList(user.getRoleList(), Role.class, RoleVo.class));
        userVo.setGroups(BeanMapper.mapList(user.getGroupList(), UserGroup.class, UserGroupVo.class));
      }
    });
  }

}
