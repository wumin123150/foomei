package com.foomei.core.service;

import com.foomei.core.dao.jpa.UserDao;
import com.foomei.core.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;

public class UserServiceTest {

  @InjectMocks
  private UserService userService;

  @Mock
  private UserDao mockUserDao;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void save() {
    User user = new User(2L);
    userService.save(user);

    // 保存超级管理用户抛出异常.
    try {
      User admin = new User(1L);
      userService.save(admin);
      failBecauseExceptionWasNotThrown(ServiceException.class);
    } catch (ServiceException e) {
      // expected exception
    }
    Mockito.verify(mockUserDao, Mockito.times(1)).save(user);
  }

}
