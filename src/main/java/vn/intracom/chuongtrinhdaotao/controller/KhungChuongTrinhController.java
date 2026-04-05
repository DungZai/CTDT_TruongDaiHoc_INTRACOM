package vn.intracom.chuongtrinhdaotao.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.intracom.chuongtrinhdaotao.dto.request.KhungChuongTrinhRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.ApiResponse;
import vn.intracom.chuongtrinhdaotao.dto.response.KhungChuongTrinhResponse;
import vn.intracom.chuongtrinhdaotao.service.IKhungChuongTrinhService;

import java.util.List;

@RestController
@RequestMapping("/api/khung-chuong-trinh")
@RequiredArgsConstructor
@Tag(name = "Khung chương trình", description = "Quản lý khung chương trình đào tạo")
public class KhungChuongTrinhController {

    private final IKhungChuongTrinhService khungService;

    @Operation(summary = "Lấy toàn bộ khung theo chương trình")
    @GetMapping("/by-chuong-trinh/{chuongTrinhId}")
    public ResponseEntity<ApiResponse<List<KhungChuongTrinhResponse>>> getByChuongTrinh(
            @PathVariable Long chuongTrinhId) {
        return ResponseEntity.ok(ApiResponse.success(khungService.getByChuongTrinh(chuongTrinhId)));
    }

    @Operation(summary = "Lấy khung theo chương trình và học kỳ")
    @GetMapping("/by-chuong-trinh/{chuongTrinhId}/hoc-ky/{hocKy}")
    public ResponseEntity<ApiResponse<List<KhungChuongTrinhResponse>>> getByHocKy(
            @PathVariable Long chuongTrinhId,
            @PathVariable Integer hocKy) {
        return ResponseEntity.ok(ApiResponse.success(khungService.getByHocKy(chuongTrinhId, hocKy)));
    }

    @Operation(summary = "Lấy khung theo nhóm kiến thức")
    @GetMapping("/by-chuong-trinh/{chuongTrinhId}/nhom-kien-thuc")
    public ResponseEntity<ApiResponse<List<KhungChuongTrinhResponse>>> getByNhomKienThuc(
            @PathVariable Long chuongTrinhId,
            @RequestParam String nhomKienThuc) {
        return ResponseEntity.ok(ApiResponse.success(khungService.getByNhomKienThuc(chuongTrinhId, nhomKienThuc)));
    }

    @Operation(summary = "Lấy tổng tín chỉ của chương trình")
    @GetMapping("/tong-tin-chi/{chuongTrinhId}")
    public ResponseEntity<ApiResponse<Integer>> getTongTinChi(@PathVariable Long chuongTrinhId) {
        return ResponseEntity.ok(ApiResponse.success(khungService.getTongTinChi(chuongTrinhId)));
    }

    @Operation(summary = "Thêm môn học vào khung chương trình")
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<KhungChuongTrinhResponse>> create(
            @Valid @RequestBody KhungChuongTrinhRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Thêm vào khung thành công", khungService.create(request)));
    }

    @Operation(summary = "Cập nhật thông tin môn trong khung")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<KhungChuongTrinhResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody KhungChuongTrinhRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công", khungService.update(id, request)));
    }

    @Operation(summary = "Xóa môn học khỏi khung chương trình")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        khungService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }
}