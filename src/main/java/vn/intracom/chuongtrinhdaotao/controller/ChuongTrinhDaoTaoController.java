package vn.intracom.chuongtrinhdaotao.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.intracom.chuongtrinhdaotao.dto.request.ChuongTrinhDaoTaoRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.ApiResponse;
import vn.intracom.chuongtrinhdaotao.dto.response.ChuongTrinhDaoTaoResponse;
import vn.intracom.chuongtrinhdaotao.service.IChuongTrinhDaoTaoService;

import java.util.List;

@RestController
@RequestMapping("/api/chuong-trinh")
@RequiredArgsConstructor
@Tag(name = "Chương trình đào tạo", description = "Quản lý chương trình đào tạo")
public class ChuongTrinhDaoTaoController {

    private final IChuongTrinhDaoTaoService chuongTrinhService;

    @Operation(summary = "Lấy tất cả chương trình đào tạo")
    @GetMapping
    public ResponseEntity<ApiResponse<List<ChuongTrinhDaoTaoResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(chuongTrinhService.getAll()));
    }

    @Operation(summary = "Lấy chương trình theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ChuongTrinhDaoTaoResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(chuongTrinhService.getById(id)));
    }

    @Operation(summary = "Lấy chương trình theo ngành")
    @GetMapping("/by-nganh/{nganhId}")
    public ResponseEntity<ApiResponse<List<ChuongTrinhDaoTaoResponse>>> getByNganh(
            @PathVariable Long nganhId) {
        return ResponseEntity.ok(ApiResponse.success(chuongTrinhService.getByNganh(nganhId)));
    }

    @Operation(summary = "Lấy chương trình theo hệ đào tạo")
    @GetMapping("/by-he/{heId}")
    public ResponseEntity<ApiResponse<List<ChuongTrinhDaoTaoResponse>>> getByHe(
            @PathVariable Long heId) {
        return ResponseEntity.ok(ApiResponse.success(chuongTrinhService.getByHe(heId)));
    }

    @Operation(summary = "Tìm kiếm chương trình theo tên")
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ChuongTrinhDaoTaoResponse>>> search(
            @RequestParam String keyword) {
        return ResponseEntity.ok(ApiResponse.success(chuongTrinhService.search(keyword)));
    }

    @Operation(summary = "Tạo chương trình đào tạo mới")
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<ChuongTrinhDaoTaoResponse>> create(
            @Valid @RequestBody ChuongTrinhDaoTaoRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo chương trình thành công", chuongTrinhService.create(request)));
    }

    @Operation(summary = "Cập nhật chương trình đào tạo")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<ChuongTrinhDaoTaoResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ChuongTrinhDaoTaoRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công", chuongTrinhService.update(id, request)));
    }

    @Operation(summary = "Xóa chương trình đào tạo")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        chuongTrinhService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }
}
