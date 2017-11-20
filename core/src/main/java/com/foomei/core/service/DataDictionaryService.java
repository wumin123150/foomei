package com.foomei.core.service;

import com.foomei.common.service.impl.JpaServiceImpl;
import com.foomei.core.dao.jpa.DataDictionaryDao;
import com.foomei.core.entity.DataDictionary;
import com.foomei.core.entity.DataType;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

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

  public List<DataDictionary> findByType(Long typeId) {
    return dataDictionaryDao.findByType(typeId);
  }

  public List<DataDictionary> findByTypeCode(String typeCode) {
    return dataDictionaryDao.findByTypeCode(typeCode);
  }

  public List<DataDictionary> findByTypeAndGrade(Long typeId, Integer grade) {
    return dataDictionaryDao.findByTypeAndGrade(typeId, grade);
  }

  public List<DataDictionary> findChildrenByParent(Long id) {
    return dataDictionaryDao.findChildrenByParent(id);
  }

  public List<DataDictionary> findChildrenByTypeAndCode(String typeCode, String code) {
    DataDictionary parent = getByTypeAndCode(typeCode, code);
    if (parent != null) {
      return dataDictionaryDao.findChildrenByParent(parent.getId());
    }
    return Lists.newArrayList();
  }

  public Page<DataDictionary> getPage(final String searchKey, final Long typeId, final Long parentId, Pageable page) {
    return dataDictionaryDao.findAll(new Specification<DataDictionary>() {
      public Predicate toPredicate(Root<DataDictionary> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<Predicate>();

        predicates.add(cb.equal(root.get(DataDictionary.PROP_TYPE).get(DataType.PROP_ID).as(Long.class), typeId));

        if (StringUtils.isNotEmpty(searchKey)) {
          Predicate p1 = cb.like(root.get(DataDictionary.PROP_CODE).as(String.class), "%" + StringUtils.trimToEmpty(searchKey) + "%");
          Predicate p2 = cb.like(root.get(DataDictionary.PROP_NAME).as(String.class), "%" + StringUtils.trimToEmpty(searchKey) + "%");
          predicates.add(cb.or(p1, p2));
        }

        if (parentId == null) {
          predicates.add(cb.isNull(root.get(DataDictionary.PROP_PARENT_ID).as(Long.class)));
        } else {
          predicates.add(cb.equal(root.get(DataDictionary.PROP_PARENT_ID).as(Long.class), parentId));
        }

        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
      }
    }, page);
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
