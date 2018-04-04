package com.foomei.core.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foomei.common.service.impl.JpaServiceImpl;
import com.foomei.common.time.DateFormatUtil;
import com.foomei.common.time.DateUtil;
import com.foomei.core.dao.jpa.LogDao;
import com.foomei.core.entity.Log;

/**
 * 日志管理业务类.
 *
 * @author walker
 */
@Service
@Transactional(readOnly = true)
public class LogService extends JpaServiceImpl<Log, String> {

  @Value("${log.period:30}")
  private int period;

  @Autowired
  private LogDao logDao;

  @Transactional(readOnly = false)
  public void save(Iterable<Log> entities) {
    logDao.save(entities);
  }

  public Page<Log> getPage(final String searchKey, final Date startTime, final Date endTime, Pageable page) {
    return logDao.findAll(new Specification<Log>() {
      public Predicate toPredicate(Root<Log> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<Predicate>();

        if (StringUtils.isNotEmpty(searchKey)) {
          Predicate p1 = cb.like(root.get(Log.PROP_USERNAME).as(String.class), "%" + StringUtils.trimToEmpty(searchKey) + "%");
          Predicate p2 = cb.like(root.get(Log.PROP_DESCRIPTION).as(String.class), "%" + StringUtils.trimToEmpty(searchKey) + "%");
          Predicate p3 = cb.like(root.get(Log.PROP_IP).as(String.class), "%" + StringUtils.trimToEmpty(searchKey) + "%");
          Predicate p4 = cb.like(root.get(Log.PROP_URL).as(String.class), "%" + StringUtils.trimToEmpty(searchKey) + "%");
          predicates.add(cb.or(p1, p2, p3, p4));
        }

        if (startTime != null && endTime != null) {
          predicates.add(cb.between(root.get(Log.PROP_LOG_TIME).as(Date.class), startTime, endTime));
        } else if (startTime != null) {
          predicates.add(cb.greaterThanOrEqualTo(root.get(Log.PROP_LOG_TIME).as(Date.class), startTime));
        } else if (endTime != null) {
          predicates.add(cb.lessThanOrEqualTo(root.get(Log.PROP_LOG_TIME).as(Date.class), endTime));
        }

        return predicates.isEmpty() ? null : cb.and(predicates.toArray(new Predicate[predicates.size()]));
      }
    }, page);
  }

  @Transactional(readOnly = false)
  public void clear() {
    Date deadline = DateUtil.subDays(DateUtil.today(), period);
    logDao.clear(deadline);
    LOGGER.info("delete entities {}, deadline is {}", entityClazz.getName(), DateFormatUtil.formatShortDate(deadline));
  }

}
