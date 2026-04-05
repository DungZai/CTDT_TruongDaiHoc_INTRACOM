package vn.intracom.chuongtrinhdaotao.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<List<RolesResponse>> getAll() {
        return ResponseEntity.ok(rolesService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RolesResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(rolesService.getById(id));
    }

    @PostMapping
    public ResponseEntity<RolesResponse> create(@RequestBody Map<String, String> body) {
        return ResponseEntity.ok(rolesService.create(body.get("roleName")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        rolesService.delete(id);
        return ResponseEntity.noContent().build();
    }
}