package com.foomei.core.service;

import com.foomei.common.collection.ListUtil;
import com.foomei.common.service.impl.MybatisServiceImpl;
import com.foomei.core.dao.mybatis.DataDictionaryMapper;
import com.foomei.core.entity.DataDictionary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DataDictionaryManager extends MybatisServiceImpl<DataDictionary, Long> {

  @Autowired
  private DataDictionaryMapper dataDictionaryMapper;

  public DataDictionary getByTypeAndCode(String typeCode, String code) {
    return dataDictionaryMapper.findByTypeAndCode(typeCode, code);
  }

  public List<DataDictionary> findByType(Long typeId) {
    return dataDictionaryMapper.findByType(typeId);
  }

  public List<DataDictionary> findByTypeCode(String typeCode) {
    return dataDictionaryMapper.findByTypeCode(typeCode);
  }

  public List<DataDictionary> findByTypeAndGrade(Long typeId, Integer grade) {
    return dataDictionaryMapper.findByTypeAndGrade(typeId, grade);
  }

  public List<DataDictionary> findChildrenByParent(Long id) {
    return dataDictionaryMapper.findChildrenByParent(id);
  }

  public List<DataDictionary> findChildrenByTypeAndCode(String typeCode, String code) {
    DataDictionary parent = getByTypeAndCode(typeCode, code);
    if (parent != null) {
      return dataDictionaryMapper.findChildrenByParent(parent.getId());
    }
    return ListUtil.newArrayList();
  }

  public Page<DataDictionary> getPage(final String searchKey, final Long typeId, final Long parentId, Pageable page) {
    return null;
  }

  public boolean existCode(Long id, String typeCode, String code) {
    DataDictionary dataDictionary = getByTypeAndCode(typeCode, code);
    if (dataDictionary == null || (id != null && id.equals(dataDictionary.getId()))) {
      return false;
    } else {
      return true;
    }
  }

}
