package com.foomei.core.web;

import com.foomei.common.mapper.BeanMapper;
import com.foomei.common.mapper.FieldsMapper;
import com.foomei.core.dto.DataDictionaryDto;
import com.foomei.core.dto.RoleDto;
import com.foomei.core.dto.UserDto;
import com.foomei.core.dto.UserGroupDto;
import com.foomei.core.entity.*;

public class BeanMapperRegister {

  public void init() {
    BeanMapper.registerClassMap(User.class, UserDto.class, new FieldsMapper<User, UserDto>() {
      @Override
      public void map(User user, UserDto userDto) {
        userDto.setRoles(BeanMapper.mapList(user.getRoleList(), Role.class, RoleDto.class));
        userDto.setGroups(BeanMapper.mapList(user.getGroupList(), UserGroup.class, UserGroupDto.class));
      }
    });

    BeanMapper.registerClassMap(DataDictionary.class, DataDictionaryDto.class, new FieldsMapper<DataDictionary, DataDictionaryDto>() {
      @Override
      public void map(DataDictionary dataDictionary, DataDictionaryDto dataDictionaryDto) {
        if(dataDictionary.getType() != null) {
          dataDictionaryDto.setTypeId(dataDictionary.getType().getId());
          dataDictionaryDto.setTypeCode(dataDictionary.getType().getCode());
        }
      }

      @Override
      public void reverseMap(DataDictionaryDto dataDictionaryDto, DataDictionary dataDictionary) {
        if(dataDictionaryDto.getTypeId() != null) {
          dataDictionary.setType(new DataType(dataDictionaryDto.getTypeId()));
        } else {
          dataDictionary.setType(null);
        }
      }
    });
  }


}
