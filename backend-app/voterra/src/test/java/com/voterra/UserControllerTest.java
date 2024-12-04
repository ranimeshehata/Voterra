package com.voterra;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.voterra.entities.User;
import com.voterra.tokenization.JwtResponse;
import com.voterra.tokenization.LoginRequest;
import com.voterra.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSignup() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");

        Mockito.when(userService.signup(Mockito.any(User.class))).thenReturn(new Object[]{user, "jwtToken"});

        mockMvc.perform(post("/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void testSignupWithFacebook() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");

        Mockito.when(userService.signupOrLoginWithGoogleOrFacebook(Mockito.any(User.class))).thenReturn(new Object[]{user, "jwtToken"});

        mockMvc.perform(post("/users/signupWithFacebook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void testSignupWithGoogle() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");

        Mockito.when(userService.signupOrLoginWithGoogleOrFacebook(Mockito.any(User.class))).thenReturn(new Object[]{user, "jwtToken"});

        mockMvc.perform(post("/users/signupWithGoogle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void testLogin() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        User user = new User();
        user.setEmail("test@example.com");

        Mockito.when(userService.login(Mockito.anyString(), Mockito.anyString())).thenReturn(new Object[]{user, "jwtToken"});

        mockMvc.perform(post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }


    @Test
    void testLoginWithGoogle() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");

        Mockito.when(userService.signupOrLoginWithGoogleOrFacebook(Mockito.any(User.class))).thenReturn(new Object[]{user, "jwtToken"});

        mockMvc.perform(post("/users/loginWithGoogle")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void testLoginWithFacebook() throws Exception {
        User user = new User();
        user.setEmail("test@example.com");

        Mockito.when(userService.signupOrLoginWithGoogleOrFacebook(Mockito.any(User.class))).thenReturn(new Object[]{user, "jwtToken"});

        mockMvc.perform(post("/users/loginWithFacebook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void testForgetPassword() throws Exception {
        LoginRequest forgetPasswordRequest = new LoginRequest();
        forgetPasswordRequest.setEmail("test@example.com");
        forgetPasswordRequest.setPassword("newPassword123");

        Mockito.when(userService.forgetPassword(Mockito.anyString(), Mockito.anyString())).thenReturn(true);

        mockMvc.perform(post("/users/forgetPassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(forgetPasswordRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }
}