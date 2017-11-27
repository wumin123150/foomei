package com.foomei.core.web.controller;

import com.foomei.core.entity.User;
import com.foomei.core.service.RoleService;
import com.foomei.core.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

public class UserControllerTest {

  @InjectMocks
  private UserController userController;

  @Mock
  private UserService userService;
  @Mock
  private RoleService roleService;

  private String theme = "ace";

  private MockMvc mockMvc;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    ReflectionTestUtils.setField(userController, "theme", theme);
  }

  @Test
  public void list() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/admin/user"))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.view().name(theme + "/user/userList"));
  }

  @Test
  public void create() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/admin/user/create"))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.view().name(theme + "/user/userForm"))
      .andExpect(MockMvcResultMatchers.model().attributeExists("roles"));
  }

  @Test
  public void update() throws Exception {
    Mockito.when(userService.get(1L)).thenReturn(new User(1L));

    mockMvc.perform(MockMvcRequestBuilders.get("/admin/user/update/{id}", 1L))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.view().name(theme + "/user/userForm"))
      .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
      .andExpect(MockMvcResultMatchers.model().attributeExists("roles"));
  }

  @Test
  public void reset() throws Exception {
    Mockito.when(userService.get(1L)).thenReturn(new User(1L));

    mockMvc.perform(MockMvcRequestBuilders.get("/admin/user/reset/{id}", 1L))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.view().name(theme + "/user/resetPassword"))
      .andExpect(MockMvcResultMatchers.model().attributeExists("user"));
  }

}
