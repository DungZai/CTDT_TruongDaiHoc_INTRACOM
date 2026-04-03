package vn.intracom.chuongtrinhdaotao.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.intracom.chuongtrinhdaotao.dto.response.ApiResponse;
import vn.intracom.chuongtrinhdaotao.dto.response.RolesResponse;
import vn.intracom.chuongtrinhdaotao.service.IRolesService;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Tag(name = "Roles", description = "Quản lý vai trò người dùng")
public class RolesController {

    private final IRolesService rolesService;

    @Operation(summary = "Lấy tất cả role")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<RolesResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(rolesService.getAll()));
    }

    @Operation(summary = "Lấy role theo ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RolesResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(rolesService.getById(id)));
    }

    @Operation(summary = "Tạo role mới")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<RolesResponse>> create(@RequestParam String roleName) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo role thành công", rolesService.create(roleName)));
    }

    @Operation(summary = "Xóa role")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        rolesService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }
}