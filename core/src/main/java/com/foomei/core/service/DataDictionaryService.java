package com.foomei.core.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foomei.common.service.impl.JpaServiceImpl;
import com.foomei.core.dao.jpa.DataDictionaryDao;
import com.foomei.core.entity.DataDictionary;
import com.google.common.collect.Lists;

/**
 * 数据字典管理业务类.
 *
 * @author walker
 */
@Service
@Transactional(readOnly = true)
public class DataDictionaryService extends JpaServiceImpl<DataDictionary, Long> {

  @Autowired
  private DataDictionaryDao dataDictionaryDao;

  public DataDictionary getByTypeAndCode(String typeCode, String code) {
    return dataDictionaryDao.findByTypeAndCode(typeCode, code);
  }

  public List<DataDictionary> findByTypeCode(String typeCode) {
    return dataDictionaryDao.findByTypeCode(typeCode);
  }

  public List<DataDictionary> findByTypeAndGrade(String typeCode, Integer grade) {
    return dataDictionaryDao.findByTypeAndGrade(typeCode, grade);
  }

  public List<DataDictionary> findChildrenByTypeAndParent(String typeCode, Long id) {
    return dataDictionaryDao.findChildrenByTypeAndParent(typeCode, id);
  }

  public List<DataDictionary> findChildrenByTypeAndCode(String typeCode, String code) {
    DataDictionary parent = getByTypeAndCode(typeCode, code);
    if (parent != null) {
      return dataDictionaryDao.findChildrenByTypeAndParent(typeCode, parent.getId());
    }
    return Lists.newArrayList();
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
