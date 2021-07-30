package com.cc.glovobe.controller;

import com.cc.glovobe.filter.JwtAccessDeniedHandler;
import com.cc.glovobe.filter.JwtAuthenticationEntryPoint;
import com.cc.glovobe.model.LoginRequest;
import com.cc.glovobe.model.RegistrationRequest;
import com.cc.glovobe.model.User;
import com.cc.glovobe.service.impl.UserServiceImpl;
import com.cc.glovobe.utility.JWTTokenProvider;
import com.cc.glovobe.utility.TokenGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.cc.glovobe.constant.UserImplConstant.REGISTRATION_COMPLETE_MESSAGE;
import static com.cc.glovobe.enumeration.Role.ROLE_USER;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @MockBean
    private UserServiceImpl userService;
    @MockBean
    JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @MockBean
    JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockBean
    JWTTokenProvider jwtTokenProvider;

    @MockBean
    TokenGenerator tokenGenerator;

    @Autowired
    private MockMvc mockMvc;


    @Test
    @DisplayName("Should register new user When making POST request to endpoint - /user/register")
    void shouldGetTokenMRegister() throws Exception {
        //given
        RegistrationRequest request = new RegistrationRequest("Alex", "Stan", "nelu@gmail.com", "123");
        when(userService.register(request)).thenReturn("token123");
        mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Alex\",\"lastName\":\"Stan\",\"email\":\"nelu@gmail.com\",\"password\":\"123\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().string(containsString("token123")))
                .andExpect(forwardedUrl(null))
                .andExpect(redirectedUrl(null))
                .andReturn().getResponse().getContentAsString();
//        when
//        then
        verify(userService).register(request);
    }

    @Test
    @DisplayName("Should confirm the token and activate the user")
    void shouldConfirmTheTokenFromEmail() throws Exception {
//        given
        String token = "token123";
        when(userService.confirmToken(token)).thenReturn(REGISTRATION_COMPLETE_MESSAGE);
        mockMvc.perform(MockMvcRequestBuilders.get("/user/confirm?token=token123"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(REGISTRATION_COMPLETE_MESSAGE)))
                .andReturn().getResponse().getContentAsString();
//        when
//        then
        verify(userService).confirmToken(token);
    }

    @Test
    @DisplayName("Should check login the user and return UserPrincipal")
    void shouldReturnUserAfterLogin() throws Exception {
//        given
        User userUnderTest = new User();
        String usernameEmail = "nelu@gmail.com";
        userUnderTest.setId(1L);
        userUnderTest.setFirstName("Alexandru");
        userUnderTest.setLastName("Stan");
        userUnderTest.setEmail(usernameEmail);
        userUnderTest.setPassword("password");
        userUnderTest.setRole(ROLE_USER.name());
        userUnderTest.setAuthorities(ROLE_USER.getAuthorities());
        userUnderTest.setIsNonLocked(true);
        userUnderTest.setEnabled(true);
        LoginRequest request = new LoginRequest("nelu@gmail.com", "123");
        when(userService.findUserByEmail(request.getEmail())).thenReturn(userUnderTest);

        mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"nelu@gmail.com\",\"password\":\"123\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(content().string(containsString("token123")))
//                .andExpect(forwardedUrl(null))
//                .andExpect(redirectedUrl(null))
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    @DisplayName("Should check delete user by id")
    void shouldDeleteUserById() throws Exception {
        //given
        mockMvc.perform(delete("/user/delete/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user("nelu@gmail.com").password("123")))
                .andExpect(status().isOk());
//      when
//      then
        verify(userService).deleteUserById(1L);
    }

}