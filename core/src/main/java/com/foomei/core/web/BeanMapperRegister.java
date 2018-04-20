package com.foomei.core.web;

import com.foomei.common.mapper.BeanMapper;
import com.foomei.common.mapper.FieldsMapper;
import com.foomei.core.dto.RoleDto;
import com.foomei.core.dto.UserDto;
import com.foomei.core.dto.UserGroupDto;
import com.foomei.core.entity.Role;
import com.foomei.core.entity.User;
import com.foomei.core.entity.UserGroup;

public class BeanMapperRegister {

  public void init() {
    BeanMapper.registerClassMap(User.class, UserDto.class, new FieldsMapper<User, UserDto>() {
      @Override
      public void map(User user, UserDto userDto) {
        userDto.setRoles(BeanMapper.mapList(user.getRoleList(), Role.class, RoleDto.class));
        userDto.setGroups(BeanMapper.mapList(user.getGroupList(), UserGroup.class, UserGroupDto.class));
      }
    });
  }

}
