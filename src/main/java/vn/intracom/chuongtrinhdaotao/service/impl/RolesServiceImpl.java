package vn.intracom.chuongtrinhdaotao.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.intracom.chuongtrinhdaotao.dto.response.RolesResponse;
import vn.intracom.chuongtrinhdaotao.entity.Roles;
import vn.intracom.chuongtrinhdaotao.exception.BadRequestException;
import vn.intracom.chuongtrinhdaotao.exception.ResourceNotFoundException;
import vn.intracom.chuongtrinhdaotao.repository.RolesRepository;
import vn.intracom.chuongtrinhdaotao.service.IRolesService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RolesServiceImpl implements IRolesService {

    private final RolesRepository rolesRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RolesResponse> getAll() {
        return rolesRepository.findAllByOrderByRoleNameAsc()
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RolesResponse getById(Long id) {
        return toResponse(findById(id));
    }

    @Override
    @Transactional
    public RolesResponse create(String roleName) {
        if (rolesRepository.existsByRoleName(roleName.toUpperCase())) {
            throw new BadRequestException("Role '" + roleName + "' đã tồn tại");
        }
        Roles role = Roles.builder()
                .roleName(roleName.toUpperCase())
                .build();
        return toResponse(rolesRepository.save(role));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        rolesRepository.delete(findById(id));
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private Roles findById(Long id) {
        return rolesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy role ID: " + id));
    }

    private RolesResponse toResponse(Roles role) {
        return RolesResponse.builder()
                .id(role.getId())
                .roleName(role.getRoleName())
                .build();
    }
}