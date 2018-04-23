package com.foomei.core.service;

import com.foomei.common.service.impl.JpaServiceImpl;
import com.foomei.core.dao.jpa.ConfigDao;
import com.foomei.core.entity.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统配置管理业务类.
 *
 * @author walker
 */
@Service
public class ConfigService extends JpaServiceImpl<Config, Long> {

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
