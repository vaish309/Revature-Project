package com.revhire.revhire.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revhire.revhire.config.JwtAuthenticationFilter;
import com.revhire.revhire.dto.LoginRequest;
import com.revhire.revhire.dto.LoginResponse;
import com.revhire.revhire.dto.RegisterJobSeekerRequest;
import com.revhire.revhire.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    // 🔥 THIS IS THE KEY FIX
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    void registerJobSeeker_Success() throws Exception {

        RegisterJobSeekerRequest request = new RegisterJobSeekerRequest();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setPassword("password123");
        request.setPhone("1234567890");

        LoginResponse response = LoginResponse.builder()
                .token("jwt-token")
                .email("john@example.com")
                .name("John Doe")
                .role("JOB_SEEKER")
                .userId(1L)
                .build();

        when(authService.registerJobSeeker(any(RegisterJobSeekerRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/auth/register/jobseeker")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.email").value("john@example.com"));
    }

    @Test
    void login_Success() throws Exception {

        LoginRequest request = new LoginRequest("john@example.com", "password123");

        LoginResponse response = LoginResponse.builder()
                .token("jwt-token")
                .email("john@example.com")
                .name("John Doe")
                .role("JOB_SEEKER")
                .userId(1L)
                .build();

        when(authService.login(any(LoginRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").value("jwt-token"));
    }
}