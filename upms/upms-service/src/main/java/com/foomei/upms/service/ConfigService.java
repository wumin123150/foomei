package com.foomei.upms.service;

import com.foomei.common.service.impl.JpaServiceImpl;
import com.foomei.upms.dao.jpa.ConfigDao;
import com.foomei.upms.entity.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统配置管理业务类.
 *
 * @author walker
 */
@Service
@Transactional(readOnly = true)
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
