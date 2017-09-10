package com.foomei.core.web.api;

import com.foomei.common.collection.ListUtil;
import com.foomei.common.dto.PageQuery;
import com.foomei.common.dto.ResponseResult;
import com.foomei.common.persistence.SearchFilter;
import com.foomei.core.entity.BaseUser;
import com.foomei.core.entity.User;
import com.foomei.core.service.BaseUserService;
import com.foomei.core.service.TokenService;
import com.foomei.core.service.UserService;
import com.google.common.net.MediaType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RequestParam;

public class UserEndpointTest {

  @InjectMocks
  private UserEndpoint userEndpoint;

  @Mock
  private BaseUserService userService;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(userEndpoint).build();
  }

  @Test
  public void page() throws Exception {
    Mockito.when(userService.getPage(Mockito.any(SearchFilter.class), Mockito.any(Pageable.class))).thenReturn(new PageImpl(ListUtil.newArrayList(new BaseUser(1L))));

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
    Mockito.when(userService.get(id)).thenReturn(new BaseUser(id));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/user/get/" + id))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.content().contentType(MediaType.JSON_UTF_8.toString()))
      .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
      .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(id));
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
