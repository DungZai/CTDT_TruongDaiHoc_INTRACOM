package vn.intracom.chuongtrinhdaotao.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.intracom.chuongtrinhdaotao.dto.response.ApiResponse;
import vn.intracom.chuongtrinhdaotao.dto.response.RolesResponse;
import vn.intracom.chuongtrinhdaotao.service.IRolesService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RolesController {

    private final IRolesService rolesService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RolesResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(rolesService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RolesResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(rolesService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RolesResponse>> create(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(ApiResponse.success(rolesService.create(body.get("roleName"))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        rolesService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}