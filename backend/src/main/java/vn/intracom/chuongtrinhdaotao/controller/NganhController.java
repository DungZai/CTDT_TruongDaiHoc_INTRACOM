package vn.intracom.chuongtrinhdaotao.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.intracom.chuongtrinhdaotao.dto.request.NganhRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.ApiResponse;
import vn.intracom.chuongtrinhdaotao.dto.response.NganhResponse;
import vn.intracom.chuongtrinhdaotao.service.INganhService;

import java.util.List;

@RestController
@RequestMapping("/api/nganh")
@RequiredArgsConstructor
@Tag(name = "Ngành", description = "Quản lý ngành đào tạo")
public class NganhController {

    private final INganhService nganhService;

    @Operation(summary = "Lấy tất cả ngành")
    @GetMapping
    public ResponseEntity<ApiResponse<List<NganhResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(nganhService.getAll()));
    }
    
    @Operation(summary = "Lấy ngành đang hoạt động (dùng cho dropdown)")
    @GetMapping("/select")
    public ResponseEntity<ApiResponse<List<NganhResponse>>> getAllForSelect() {
    return ResponseEntity.ok(ApiResponse.success(nganhService.getAllForSelect()));
    }

    @Operation(summary = "Lấy ngành theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NganhResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(nganhService.getById(id)));
    }

    @Operation(summary = "Lấy danh sách ngành đang hoạt động")
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<NganhResponse>>> getActive() {
        return ResponseEntity.ok(ApiResponse.success(nganhService.getByTrangThai(true)));
    }

    @Operation(summary = "Tạo ngành mới")
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<NganhResponse>> create(
            @Valid @RequestBody NganhRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo ngành thành công", nganhService.create(request)));
    }

    @Operation(summary = "Cập nhật ngành")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<NganhResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody NganhRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công", nganhService.update(id, request)));
    }

    @Operation(summary = "Xóa ngành")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        nganhService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }
}