package com.foomei.core.dao.jpa;

import com.foomei.common.persistence.DynamicSpecification;
import com.foomei.common.persistence.SearchFilter;
import com.foomei.common.test.spring.SpringTransactionalTestCase;
import com.foomei.core.entity.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext
@ContextConfiguration(locations = {"/applicationContext.xml"})
public class UserDaoTest extends SpringTransactionalTestCase {

  @Autowired
  private UserDao userDao;

  @Test
  public void findByLoginName() {
    User user = userDao.findByLoginName("admin");
    assertThat(user).isNotNull();
  }

}
