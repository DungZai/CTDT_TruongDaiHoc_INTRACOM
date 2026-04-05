package vn.intracom.chuongtrinhdaotao.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.intracom.chuongtrinhdaotao.dto.request.HeDaoTaoRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.ApiResponse;
import vn.intracom.chuongtrinhdaotao.dto.response.HeDaoTaoResponse;
import vn.intracom.chuongtrinhdaotao.service.IHeDaoTaoService;

import java.util.List;

@RestController
@RequestMapping("/api/he-dao-tao")
@RequiredArgsConstructor
@Tag(name = "Hệ đào tạo", description = "Quản lý hệ đào tạo")
public class HeDaoTaoController {

    private final IHeDaoTaoService heDaoTaoService;

    @Operation(summary = "Lấy tất cả hệ đào tạo")
    @GetMapping
    public ResponseEntity<ApiResponse<List<HeDaoTaoResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(heDaoTaoService.getAll()));
    }

    @Operation(summary = "Lấy hệ đào tạo theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HeDaoTaoResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(heDaoTaoService.getById(id)));
    }

    @Operation(summary = "Tạo hệ đào tạo mới")
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<HeDaoTaoResponse>> create(
            @Valid @RequestBody HeDaoTaoRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Tạo hệ đào tạo thành công", heDaoTaoService.create(request)));
    }

    @Operation(summary = "Cập nhật hệ đào tạo")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<HeDaoTaoResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody HeDaoTaoRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Cập nhật thành công", heDaoTaoService.update(id, request)));
    }

    @Operation(summary = "Xóa hệ đào tạo")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        heDaoTaoService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }
}
