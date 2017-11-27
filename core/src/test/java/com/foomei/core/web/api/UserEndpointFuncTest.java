package com.foomei.core.web.api;

import com.foomei.common.security.shiro.ShiroUser;
import com.foomei.common.test.security.shiro.ShiroTestUtils;
import com.foomei.common.test.spring.SpringTransactionalTestCase;
import com.google.common.net.MediaType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@DirtiesContext
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:applicationContext.xml", "classpath:applicationContext-plugin.xml", "file:src/main/webapp/WEB-INF/spring-mvc.xml" })
public class UserEndpointFuncTest extends SpringTransactionalTestCase {

  @Autowired
  private WebApplicationContext context;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    ShiroTestUtils.mockSubject(new ShiroUser(1L, "admin", "Admin"));
  }

  @After
  public void tearDown() {
    ShiroTestUtils.clearSubject();
  }

  @Test
  public void page() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/user/page").param("searchKey", "admin"))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.content().contentType(MediaType.JSON_UTF_8.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
      .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalElements").value(1L));
  }

  @Test
  public void get() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/user/get/{id}", 1L))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.content().contentType(MediaType.JSON_UTF_8.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
      .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(1L));
  }

  @Test
  public void create() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/api/user/create").param("loginName", "wumin").param("plainPassword", "123456").param("name", "wumin").param("status", "A"))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.content().contentType(MediaType.JSON_UTF_8.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(false));
  }

  @Test
  public void update() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/api/user/update").param("id", "2").param("loginName", "wumin").param("name", "wumin").param("status", "A"))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.content().contentType(MediaType.JSON_UTF_8.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
  }

  @Test
  public void reset() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/api/user/reset").param("userId", "2").param("password", "123456"))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.content().contentType(MediaType.JSON_UTF_8.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
  }

  @Test
  public void delete() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/api/user/delete/{id}", 2L))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.content().contentType(MediaType.JSON_UTF_8.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
  }

  @Test
  public void start() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.post("/api/user/start/{id}", 2L))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.content().contentType(MediaType.JSON_UTF_8.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
  }

  @Test
  public void checkLoginName() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/api/user/checkLoginName").param("id", "").param("loginName", "admin"))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.content().contentType(MediaType.JSON_UTF_8.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$").value(false));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/user/checkLoginName").param("id", "1").param("loginName", "admin"))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.content().contentType(MediaType.JSON_UTF_8.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$").value(true));
  }

}
