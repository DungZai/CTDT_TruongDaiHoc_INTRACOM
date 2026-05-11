package vn.intracom.chuongtrinhdaotao.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.intracom.chuongtrinhdaotao.dto.request.MonHocChuanDauRaRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.ApiResponse;
import vn.intracom.chuongtrinhdaotao.dto.response.MonHocChuanDauRaResponse;
import vn.intracom.chuongtrinhdaotao.service.IMonHocChuanDauRaService;

import java.util.List;

@RestController
@RequestMapping("/api/mon-hoc-chuan-dau-ra")
@RequiredArgsConstructor
@Tag(name = "Ma trận CLO", description = "Quản lý liên kết Môn học × Chuẩn đầu ra")
public class MonHocChuanDauRaController {

    private final IMonHocChuanDauRaService matranService;

    @Operation(summary = "Lấy ma trận theo môn học")
    @GetMapping("/mon-hoc/{monHocId}")
    public ResponseEntity<ApiResponse<List<MonHocChuanDauRaResponse>>> getByMonHoc(
            @PathVariable Long monHocId) {
        return ResponseEntity.ok(
                ApiResponse.success(matranService.getByMonHoc(monHocId)));
    }

    @Operation(summary = "Lấy ma trận theo chuẩn đầu ra")
    @GetMapping("/chuan-dau-ra/{chuanDauRaId}")
    public ResponseEntity<ApiResponse<List<MonHocChuanDauRaResponse>>> getByChuanDauRa(
            @PathVariable Long chuanDauRaId) {
        return ResponseEntity.ok(
                ApiResponse.success(matranService.getByChuanDauRa(chuanDauRaId)));
    }

    @Operation(summary = "Thêm liên kết môn học - chuẩn đầu ra")
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<MonHocChuanDauRaResponse>> create(
            @Valid @RequestBody MonHocChuanDauRaRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Thêm thành công",
                        matranService.create(request)));
    }

    @Operation(summary = "Cập nhật mức độ CLO")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<MonHocChuanDauRaResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody MonHocChuanDauRaRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success("Cập nhật thành công",
                        matranService.update(id, request)));
    }

    @Operation(summary = "Xóa liên kết môn học - chuẩn đầu ra")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        matranService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }
}