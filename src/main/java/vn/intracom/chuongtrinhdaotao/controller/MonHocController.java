package vn.intracom.chuongtrinhdaotao.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.intracom.chuongtrinhdaotao.dto.request.MonHocRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.ApiResponse;
import vn.intracom.chuongtrinhdaotao.dto.response.MonHocResponse;
import vn.intracom.chuongtrinhdaotao.service.IMonHocService;

import java.util.List;

@RestController
@RequestMapping("/api/mon-hoc")
@RequiredArgsConstructor
@Tag(name = "Môn học", description = "Quản lý môn học")
public class MonHocController {

    private final IMonHocService monHocService;

    @Operation(summary = "Lấy tất cả môn học")
    @GetMapping
    public ResponseEntity<ApiResponse<List<MonHocResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(monHocService.getAll()));
    }

    @Operation(summary = "Lấy môn học theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MonHocResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(monHocService.getById(id)));
    }

    @Operation(summary = "Lấy môn học đang hoạt động")
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<MonHocResponse>>> getActive() {
        return ResponseEntity.ok(ApiResponse.success(monHocService.getByTrangThai(true)));
    }

    @Operation(summary = "Tìm kiếm môn học theo tên")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<MonHocResponse>>> search(
            @RequestParam String keyword) {
        return ResponseEntity.ok(ApiResponse.success(monHocService.search(keyword)));
    }

    @Operation(summary = "Lấy môn học theo chương trình đào tạo")
    @GetMapping("/by-chuong-trinh/{chuongTrinhId}")
    public ResponseEntity<ApiResponse<List<MonHocResponse>>> getByChuongTrinh(
            @PathVariable Long chuongTrinhId) {
        return ResponseEntity.ok(ApiResponse.success(monHocService.getByChuongTrinh(chuongTrinhId)));
    }

    @Operation(summary = "Tạo môn học mới")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MonHocResponse>> create(
            @Valid @RequestBody MonHocRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo môn học thành công", monHocService.create(request)));
    }

    @Operation(summary = "Cập nhật môn học")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MonHocResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody MonHocRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công", monHocService.update(id, request)));
    }

    @Operation(summary = "Xóa môn học")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        monHocService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }
}