package com.foomei.core.web.api;

import com.foomei.common.collection.ListUtil;
import com.foomei.common.persistence.search.SearchRequest;
import com.foomei.core.entity.User;
import com.foomei.core.service.UserService;
import com.google.common.net.MediaType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class UserEndpointTest {

  @InjectMocks
  private UserEndpoint userEndpoint;

  @Mock
  private UserService userService;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(userEndpoint).build();
  }

  @Test
  public void page() throws Exception {
    Mockito.when(userService.getPage(Mockito.any(SearchRequest.class))).thenReturn(new PageImpl(ListUtil.newArrayList(new User(1L))));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/user/page").param("searchKey", "admin"))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.content().contentType(MediaType.JSON_UTF_8.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
      .andExpect(MockMvcResultMatchers.jsonPath("$.data.totalElements").value(1L));
  }

  @Test
  public void get() throws Exception {
    Long id = 1L;
    Mockito.when(userService.get(id)).thenReturn(new User(id));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/user/get/{id}", id))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.content().contentType(MediaType.JSON_UTF_8.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
      .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(id));
  }

  @Test
  public void create() throws Exception {
    Mockito.when(userService.existLoginName(null, "wumin")).thenReturn(false);
    Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(new User(2L));

    mockMvc.perform(MockMvcRequestBuilders.post("/api/user/create").param("loginName", "wumin").param("plainPassword", "123456").param("name", "wumin").param("status", "A"))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.content().contentType(MediaType.JSON_UTF_8.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
  }

  @Test
  public void update() throws Exception {
    Mockito.when(userService.get(2L)).thenReturn(new User(2L));
    Mockito.when(userService.existLoginName(2L, "wumin")).thenReturn(false);
    Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(new User(2L));

    mockMvc.perform(MockMvcRequestBuilders.post("/api/user/update").param("id", "2").param("loginName", "wumin").param("name", "wumin").param("status", "A"))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.content().contentType(MediaType.JSON_UTF_8.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
  }

  @Test
  public void reset() throws Exception {
    Mockito.when(userService.getByLoginName("wumin")).thenReturn(new User(2L));
    Mockito.doNothing().when(userService).changePassword(Mockito.eq("2"), Mockito.eq("123456"));

    mockMvc.perform(MockMvcRequestBuilders.post("/api/user/reset").param("userId", "2").param("password", "123456"))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.content().contentType(MediaType.JSON_UTF_8.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
  }

  @Test
  public void delete() throws Exception {
    Mockito.doNothing().when(userService).delete(Mockito.eq(2L));

    mockMvc.perform(MockMvcRequestBuilders.post("/api/user/delete/{id}", 2L))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.content().contentType(MediaType.JSON_UTF_8.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
  }

  @Test
  public void start() throws Exception {
    Mockito.doNothing().when(userService).start(Mockito.eq(2L));

    mockMvc.perform(MockMvcRequestBuilders.post("/api/user/start/{id}", 2L))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.content().contentType(MediaType.JSON_UTF_8.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true));
  }

  @Test
  public void checkLoginName() throws Exception {
    Mockito.when(userService.existLoginName(null, "admin")).thenReturn(true);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/user/checkLoginName").param("id", "").param("loginName", "admin"))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.content().contentType(MediaType.JSON_UTF_8.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$").value(false));

    Mockito.when(userService.existLoginName(1L, "admin")).thenReturn(false);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/user/checkLoginName").param("id", "1").param("loginName", "admin"))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.content().contentType(MediaType.JSON_UTF_8.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$").value(true));
  }

}
