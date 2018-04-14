package com.foomei.upms.service;

import com.foomei.common.service.impl.JpaServiceImpl;
import com.foomei.common.time.DateUtil;
import com.foomei.upms.dao.jpa.TokenDao;
import com.foomei.upms.entity.Token;
import com.foomei.upms.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 令牌管理业务类.
 *
 * @author walker
 */
@Service
@Transactional(readOnly = true)
public class TokenService extends JpaServiceImpl<Token, String> {

  @Autowired
  private TokenDao tokenDao;

  @Transactional(readOnly = false)
  public void disable(Long userId) {
    tokenDao.disable(userId);
    LOGGER.info("disable entity {}, userId is {}", Token.class.getName(), userId);
  }

  @Transactional(readOnly = false)
  public String apply(Long userId, String terminal, String remark) {
    disable(userId);

    Token token = new Token();
    token.setUser(new User(userId));
    token.setTerminal(terminal);
    token.setRemark(remark);
    token.setCreateTime(new Date());
    token.setExpireTime(DateUtil.addDays(new Date(), Token.DEFAULT_EXPIRE));
    token.setStatus(Token.STATUS_ENABLE);

    save(token);

    return token.getId();
  }

  public boolean activate(Long userId) {
    List<Token> tokens = tokenDao.findByEnable(userId);
    return !tokens.isEmpty();
  }

}
