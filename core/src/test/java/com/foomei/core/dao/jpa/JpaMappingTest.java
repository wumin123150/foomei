package com.foomei.core.dao.jpa;

import com.foomei.common.test.spring.SpringTransactionalTestCase;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(locations = { "/applicationContext.xml" })
public class JpaMappingTest extends SpringTransactionalTestCase {

  private static Logger logger = LoggerFactory.getLogger(JpaMappingTest.class);

  @PersistenceContext
  private EntityManager em;

  @Test
  public void allClassMapping() throws Exception {
    Metamodel model = em.getEntityManagerFactory().getMetamodel();
    assertThat(model.getEntities()).as("No entity mapping found").isNotEmpty();

    for (EntityType entityType : model.getEntities()) {
      String entityName = entityType.getName();
      em.createQuery("select o from " + entityName + " o").getResultList();
      logger.info("ok: " + entityName);
    }
  }
}