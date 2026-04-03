package vn.intracom.chuongtrinhdaotao.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.intracom.chuongtrinhdaotao.dto.request.DeCuongMonHocRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.ApiResponse;
import vn.intracom.chuongtrinhdaotao.dto.response.DeCuongMonHocResponse;
import vn.intracom.chuongtrinhdaotao.service.IDeCuongMonHocService;

@RestController
@RequestMapping("/api/de-cuong")
@RequiredArgsConstructor
@Tag(name = "Đề cương môn học", description = "Quản lý đề cương chi tiết môn học")
public class DeCuongMonHocController {

    private final IDeCuongMonHocService deCuongService;

    @Operation(summary = "Lấy đề cương theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DeCuongMonHocResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(deCuongService.getById(id)));
    }

    @Operation(summary = "Lấy đề cương theo môn học")
    @GetMapping("/by-mon-hoc/{monHocId}")
    public ResponseEntity<ApiResponse<DeCuongMonHocResponse>> getByMonHoc(
            @PathVariable Long monHocId) {
        return ResponseEntity.ok(ApiResponse.success(deCuongService.getByMonHoc(monHocId)));
    }

    @Operation(summary = "Tạo đề cương môn học mới")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DeCuongMonHocResponse>> create(
            @Valid @RequestBody DeCuongMonHocRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo đề cương thành công", deCuongService.create(request)));
    }

    @Operation(summary = "Cập nhật đề cương môn học")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DeCuongMonHocResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody DeCuongMonHocRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công", deCuongService.update(id, request)));
    }

    @Operation(summary = "Xóa đề cương môn học")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        deCuongService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }
}