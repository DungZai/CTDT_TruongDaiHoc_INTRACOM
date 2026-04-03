package vn.intracom.chuongtrinhdaotao.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.intracom.chuongtrinhdaotao.dto.request.ChuanDauRaRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.ApiResponse;
import vn.intracom.chuongtrinhdaotao.dto.response.ChuanDauRaResponse;
import vn.intracom.chuongtrinhdaotao.service.IChuanDauRaService;

import java.util.List;

@RestController
@RequestMapping("/api/chuan-dau-ra")
@RequiredArgsConstructor
@Tag(name = "Chuẩn đầu ra", description = "Quản lý chuẩn đầu ra chương trình")
public class ChuanDauRaController {

    private final IChuanDauRaService chuanDauRaService;

    @Operation(summary = "Lấy tất cả chuẩn đầu ra theo chương trình")
    @GetMapping("/by-chuong-trinh/{chuongTrinhId}")
    public ResponseEntity<ApiResponse<List<ChuanDauRaResponse>>> getByChuongTrinh(
            @PathVariable Long chuongTrinhId) {
        return ResponseEntity.ok(ApiResponse.success(chuanDauRaService.getByChuongTrinh(chuongTrinhId)));
    }

    @Operation(summary = "Lấy chuẩn đầu ra theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ChuanDauRaResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(chuanDauRaService.getById(id)));
    }

    @Operation(summary = "Tạo chuẩn đầu ra mới")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ChuanDauRaResponse>> create(
            @Valid @RequestBody ChuanDauRaRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo chuẩn đầu ra thành công", chuanDauRaService.create(request)));
    }

    @Operation(summary = "Cập nhật chuẩn đầu ra")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ChuanDauRaResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ChuanDauRaRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công", chuanDauRaService.update(id, request)));
    }

    @Operation(summary = "Xóa chuẩn đầu ra")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        chuanDauRaService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }
}