package com.foomei.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foomei.common.service.impl.JpaServiceImpl;
import com.foomei.core.dao.jpa.ConfigDao;
import com.foomei.core.entity.Config;

/**
 * 系统配置管理业务类.
 *
 * @author walker
 */
@Service
@Transactional(readOnly = true)
public class ConfigService extends JpaServiceImpl<ConfigDao, Config, Long> {

  @Autowired
  private ConfigDao configDao;

  public Config getByCode(String code) {
    return configDao.findByCode(code);
  }

  public boolean existCode(Long id, String code) {
    Config config = getByCode(code);
    if (config == null || (id != null && id.equals(config.getId()))) {
      return false;
    } else {
      return true;
    }
  }

}
