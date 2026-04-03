package vn.intracom.chuongtrinhdaotao.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import vn.intracom.chuongtrinhdaotao.dto.request.LoginRequest;
import vn.intracom.chuongtrinhdaotao.dto.request.RegisterRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.ApiResponse;
import vn.intracom.chuongtrinhdaotao.dto.response.JwtResponse;
import vn.intracom.chuongtrinhdaotao.security.JwtTokenProvider;
import vn.intracom.chuongtrinhdaotao.service.IUserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Đăng nhập, đăng ký")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final IUserService userService;

    @Operation(summary = "Đăng nhập, trả về JWT token")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        JwtResponse jwtResponse = userService.buildJwtResponse(userDetails.getUsername(), token);

        return ResponseEntity.ok(ApiResponse.success("Đăng nhập thành công", jwtResponse));
    }

    @Operation(summary = "Đăng ký tài khoản mới")
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(
            @Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.ok(ApiResponse.success("Đăng ký thành công", null));
    }
}