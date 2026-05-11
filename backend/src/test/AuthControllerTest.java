package vn.intracom.chuongtrinhdaotao;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import vn.intracom.chuongtrinhdaotao.dto.request.LoginRequest;
import vn.intracom.chuongtrinhdaotao.dto.request.RegisterRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.JwtResponse;
import vn.intracom.chuongtrinhdaotao.security.JwtTokenProvider;
import vn.intracom.chuongtrinhdaotao.service.IUserService;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockBean private IUserService userService;
    @MockBean private JwtTokenProvider jwtTokenProvider;

    @Test
    void login_withValidCredentials_shouldReturnToken() throws Exception {
        LoginRequest loginRequest = new LoginRequest("admin", "123456");

        JwtResponse jwtResponse = JwtResponse.builder()
                .token("mock-jwt-token")
                .type("Bearer")
                .id(1L)
                .username("admin")
                .email("admin@intracom.edu.vn")
                .role("ADMIN")
                .build();

        when(userService.buildJwtResponse(eq("admin"), any())).thenReturn(jwtResponse);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.username").value("admin"))
                .andExpect(jsonPath("$.data.role").value("ADMIN"));
    }

    @Test
    void login_withBlankUsername_shouldReturn400() throws Exception {
        LoginRequest loginRequest = new LoginRequest("", "123456");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.username").exists());
    }

    @Test
    void register_withValidData_shouldReturn200() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest(
                "newuser", "password123", "newuser@intracom.edu.vn", null);

        doNothing().when(userService).register(any());

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Đăng ký thành công"));
    }

    @Test
    void register_withInvalidEmail_shouldReturn400() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest(
                "newuser", "password123", "not-an-email", null);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.email").exists());
    }

    @Test
    void register_withShortPassword_shouldReturn400() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest(
                "newuser", "123", "newuser@intracom.edu.vn", null);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.password").exists());
    }
}