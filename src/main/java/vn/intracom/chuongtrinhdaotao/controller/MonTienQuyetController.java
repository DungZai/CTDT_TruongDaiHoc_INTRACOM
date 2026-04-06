package vn.intracom.chuongtrinhdaotao.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.intracom.chuongtrinhdaotao.dto.request.MonTienQuyetRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.ApiResponse;
import vn.intracom.chuongtrinhdaotao.dto.response.MonTienQuyetResponse;
import vn.intracom.chuongtrinhdaotao.service.IMonTienQuyetService;

import java.util.List;

@RestController
@RequestMapping("/api/mon-tien-quyet")
@RequiredArgsConstructor
@Tag(name = "Môn tiên quyết", description = "Quản lý môn tiên quyết")
public class MonTienQuyetController {

    private final IMonTienQuyetService monTienQuyetService;

    @Operation(summary = "Lấy danh sách môn tiên quyết theo môn học")
    @GetMapping("/by-mon/{monHocId}")
    public ResponseEntity<ApiResponse<List<MonTienQuyetResponse>>> getByMonHoc(
            @PathVariable Long monHocId) {
        return ResponseEntity.ok(
                ApiResponse.success(monTienQuyetService.getByMonHoc(monHocId)));
    }

    @Operation(summary = "Thêm môn tiên quyết")
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<MonTienQuyetResponse>> create(
            @Valid @RequestBody MonTienQuyetRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Thêm môn tiên quyết thành công",
                        monTienQuyetService.create(request)));
    }

    @Operation(summary = "Xóa môn tiên quyết")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        monTienQuyetService.delete(id);
        return ResponseEntity.ok(ApiResponse.success("Xóa thành công", null));
    }
}