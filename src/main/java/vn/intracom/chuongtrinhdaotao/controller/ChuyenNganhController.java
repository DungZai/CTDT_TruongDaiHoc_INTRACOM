package vn.intracom.chuongtrinhdaotao.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.intracom.chuongtrinhdaotao.dto.request.ChuyenNganhRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.ApiResponse;
import vn.intracom.chuongtrinhdaotao.dto.response.ChuyenNganhResponse;
import vn.intracom.chuongtrinhdaotao.dto.response.PageResponse;
import vn.intracom.chuongtrinhdaotao.service.IChuyenNganhService;

import java.util.List;

@RestController
@RequestMapping("/api/chuyen-nganh")
@RequiredArgsConstructor
@Tag(name = "Chuyên ngành", description = "Quản lý chuyên ngành")
public class ChuyenNganhController {

    private final IChuyenNganhService chuyenNganhService;


    @Operation(summary = "Lấy danh sách chuyên ngành (có phân trang)")
@GetMapping
public ResponseEntity<ApiResponse<PageResponse<ChuyenNganhResponse>>> getAll(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) Long nganhId,
        @RequestParam(defaultValue = "0")  int page,
        @RequestParam(defaultValue = "10") int size) {
    return ResponseEntity.ok(
            ApiResponse.success(chuyenNganhService.getAll(keyword, nganhId, page, size))
    );
}

@Operation(summary = "Lấy chuyên ngành đang hoạt động (dùng cho dropdown)")
@GetMapping("/select")
public ResponseEntity<ApiResponse<List<ChuyenNganhResponse>>> getAllForSelect() {
    return ResponseEntity.ok(ApiResponse.success(chuyenNganhService.getAllForSelect()));
}

    @Operation(summary = "Lấy chuyên ngành theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ChuyenNganhResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(chuyenNganhService.getById(id)));
    }

    @Operation(summary = "Lấy chuyên ngành theo ngành")
    @GetMapping("/by-nganh/{nganhId}")
    public ResponseEntity<ApiResponse<List<ChuyenNganhResponse>>> getByNganh(
            @PathVariable Long nganhId) {
        return ResponseEntity.ok(ApiResponse.success(chuyenNganhService.getByNganh(nganhId)));
    }

    @Operation(summary = "Tạo chuyên ngành mới")
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<ChuyenNganhResponse>> create(
            @Valid @RequestBody ChuyenNganhRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo chuyên ngành thành công", chuyenNganhService.create(request)));
    }

    @Operation(summary = "Cập nhật chuyên ngành")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<ChuyenNganhResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody ChuyenNganhRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công", chuyenNganhService.update(id, request)));
    }

    @Operation(summary = "Xóa chuyên ngành")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        chuyenNganhService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }
}
