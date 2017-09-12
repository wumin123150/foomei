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
import org.springframework.http.MediaType;
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

  private MockMvc mockMvc;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
  }

  @Test
  public void list() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/admin/user"))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.view().name("admin/user/userList"));
  }

  @Test
  public void createForm() throws Exception {
    mockMvc.perform(MockMvcRequestBuilders.get("/admin/user/create"))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.view().name("admin/user/userForm"))
      .andExpect(MockMvcResultMatchers.model().attributeExists("roles"));
  }

  @Test
  public void create() throws Exception {
    Mockito.when(userService.existLoginName(null, "wumin")).thenReturn(false);
    Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(new User(2L));

    mockMvc.perform(MockMvcRequestBuilders.post("/admin/user/create").contentType(MediaType.MULTIPART_FORM_DATA).param("loginName", "wumin").param("plainPassword", "123456").param("name", "wumin"))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isFound())
      .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/user"));
  }

  @Test
  public void updateForm() throws Exception {
    Mockito.when(userService.get(1L)).thenReturn(new User(1L));

    mockMvc.perform(MockMvcRequestBuilders.get("/admin/user/update/{id}", 1L))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.view().name("admin/user/userForm"))
      .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
      .andExpect(MockMvcResultMatchers.model().attributeExists("roles"));
  }

  @Test
  public void update() throws Exception {
    Mockito.when(userService.get(2L)).thenReturn(new User(2L));
    Mockito.when(userService.existLoginName(2L, "wumin")).thenReturn(false);
    Mockito.when(userService.save(Mockito.any(User.class))).thenReturn(new User(2L));

    mockMvc.perform(MockMvcRequestBuilders.post("/admin/user/update").contentType(MediaType.MULTIPART_FORM_DATA).param("id", "2").param("loginName", "wumin").param("plainPassword", "123456").param("name", "wumin"))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isFound())
      .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/user"));
  }

  @Test
  public void resetForm() throws Exception {
    Mockito.when(userService.get(1L)).thenReturn(new User(1L));

    mockMvc.perform(MockMvcRequestBuilders.get("/admin/user/reset/{id}", 1L))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isOk())
      .andExpect(MockMvcResultMatchers.view().name("admin/user/resetPassword"))
      .andExpect(MockMvcResultMatchers.model().attributeExists("user"));
  }

  @Test
  public void reset() throws Exception {
    Mockito.when(userService.getByLoginName("wumin")).thenReturn(new User(2L));
    Mockito.doNothing().when(userService).changePassword(Mockito.eq("wumin"), Mockito.eq("123456"));

    mockMvc.perform(MockMvcRequestBuilders.post("/admin/user/reset").param("loginName", "wumin").param("plainPassword", "123456"))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isFound())
      .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/user"));
  }

  @Test
  public void delete() throws Exception {
    Mockito.doNothing().when(userService).delete(Mockito.eq(2L));

    mockMvc.perform(MockMvcRequestBuilders.post("/admin/user/delete/{id}", 2L))
      .andDo(MockMvcResultHandlers.print())
      .andExpect(MockMvcResultMatchers.status().isFound())
      .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/user"));
  }

}
