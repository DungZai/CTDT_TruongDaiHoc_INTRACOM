package vn.intracom.chuongtrinhdaotao.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import vn.intracom.chuongtrinhdaotao.dto.request.RegisterRequest;
import vn.intracom.chuongtrinhdaotao.dto.request.UserUpdateRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.ApiResponse;
import vn.intracom.chuongtrinhdaotao.dto.response.PageResponse;
import vn.intracom.chuongtrinhdaotao.dto.response.UserResponse;
import vn.intracom.chuongtrinhdaotao.service.IUserService;




@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Quản lý người dùng")
public class UserController {

    private final IUserService userService;

    @Operation(summary = "Lấy thông tin người dùng đang đăng nhập")
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMe(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(
                userService.getByUsername(userDetails.getUsername())));
    }

    @Operation(summary = "Lấy danh sách người dùng (có phân trang)")
   @GetMapping
@PreAuthorize("hasAuthority('ADMIN')")
public ResponseEntity<ApiResponse<PageResponse<UserResponse>>> getAll(
        @RequestParam(required = false) String keyword,
        @RequestParam(defaultValue = "0")  int page,
        @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.ok(
            ApiResponse.success(userService.getAll(keyword, page, size))
    );
}

    @Operation(summary = "Lấy người dùng theo ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.getById(id)));
    }

    @Operation(summary = "Tạo người dùng mới")
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> create(
            @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo tài khoản thành công",
                        userService.create(request)));
    }

    @Operation(summary = "Cập nhật người dùng")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<UserResponse>> update(
            @PathVariable Long id,
            @RequestBody UserUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công",
                userService.update(id, request)));
    }

    @Operation(summary = "Xóa người dùng")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }
}