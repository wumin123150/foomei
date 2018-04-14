package com.foomei.upms.service;

import com.foomei.common.collection.ListUtil;
import com.foomei.common.service.impl.JpaServiceImpl;
import com.foomei.upms.dao.jpa.UserGroupDao;
import com.foomei.upms.entity.UserGroup;
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
 * 用户组管理业务类.
 *
 * @author walker
 */
@Service
@Transactional(readOnly = true)
public class UserGroupService extends JpaServiceImpl<UserGroup, Long> {

  @Autowired
  private UserGroupDao userGroupDao;

  public UserGroup getByCode(String code) {
    return userGroupDao.findByCode(code);
  }

  public List<UserGroup> getAll() {
    return userGroupDao.findAll();
  }

  public List<UserGroup> findTop() {
    return userGroupDao.findTop();
  }

  public List<UserGroup> findTreeByPath(String path) {
    return userGroupDao.findByPath(path + UserGroup.PATH_SPLIT + "%");
  }

  public List<UserGroup> findChildrenByParent(Long id) {
    return userGroupDao.findChildrenByParent(id);
  }

  public List<UserGroup> findChildrenByCode(String code) {
    UserGroup parent = getByCode(code);
    if (parent != null) {
      return findChildrenByParent(parent.getId());
    }
    return ListUtil.newArrayList();
  }

  @Transactional(readOnly = false)
  public UserGroup save(UserGroup userGroup) {
    if (userGroup.getId() == null) {
      userGroup.setDelFlag(false);
    }

    String srcPath = userGroup.getPath();
    if (!StringUtils.endsWith(srcPath, UserGroup.PATH_SPLIT + userGroup.getCode())) {
      List<UserGroup> children = findTreeByPath(userGroup.getPath());

      String tarPath = StringUtils.substringBeforeLast(srcPath, UserGroup.PATH_SPLIT) + UserGroup.PATH_SPLIT + userGroup.getCode();
      for (UserGroup child : children) {
        String path = tarPath + StringUtils.substring(child.getPath(), srcPath.length());
        child.setPath(path);
        super.save(child);
      }
      userGroup.setPath(tarPath);
    }

    return super.save(userGroup);
  }

  public Page<UserGroup> getPage(final String searchKey, final Long parentId, Pageable page) {
    return userGroupDao.findAll(new Specification<UserGroup>() {
      public Predicate toPredicate(Root<UserGroup> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<Predicate>();

        predicates.add(cb.equal(root.get(UserGroup.PROP_DEL_FLAG).as(Boolean.class), false));

        if (StringUtils.isNotEmpty(searchKey)) {
          Predicate p1 = cb.like(root.get(UserGroup.PROP_CODE).as(String.class), "%" + StringUtils.trimToEmpty(searchKey) + "%");
          Predicate p2 = cb.like(root.get(UserGroup.PROP_NAME).as(String.class), "%" + StringUtils.trimToEmpty(searchKey) + "%");
          predicates.add(cb.or(p1, p2));
        }

        if (parentId == null) {
          predicates.add(cb.isNull(root.get(UserGroup.PROP_PARENT_ID).as(Long.class)));
        } else {
          predicates.add(cb.equal(root.get(UserGroup.PROP_PARENT_ID).as(Long.class), parentId));
        }

        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
      }
    }, page);
  }

  public boolean existCode(Long id, String code) {
    UserGroup userGroup = getByCode(code);
    if (userGroup == null || (id != null && id.equals(userGroup.getId()))) {
      return false;
    } else {
      return true;
    }
  }

}
