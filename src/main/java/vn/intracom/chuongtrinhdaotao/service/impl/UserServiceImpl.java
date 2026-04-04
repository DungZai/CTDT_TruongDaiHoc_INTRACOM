package vn.intracom.chuongtrinhdaotao.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.intracom.chuongtrinhdaotao.dto.request.RegisterRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.JwtResponse;
import vn.intracom.chuongtrinhdaotao.dto.response.UserResponse;
import vn.intracom.chuongtrinhdaotao.entity.Roles;
import vn.intracom.chuongtrinhdaotao.entity.Users;
import vn.intracom.chuongtrinhdaotao.exception.BadRequestException;
import vn.intracom.chuongtrinhdaotao.exception.ResourceNotFoundException;
import vn.intracom.chuongtrinhdaotao.repository.RolesRepository;
import vn.intracom.chuongtrinhdaotao.repository.UserRepository;
import vn.intracom.chuongtrinhdaotao.service.IUserService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAll() {
        return userRepository.findAll()
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getById(Long id) {
        return toResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getByUsername(String username) {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user: " + username));
        return toResponse(user);
    }

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username '" + request.getUsername() + "' đã tồn tại");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email '" + request.getEmail() + "' đã được sử dụng");
        }

        // Mặc định gán role USER nếu không truyền roleId
        Long roleId = request.getRoleId() != null ? request.getRoleId() : getDefaultRoleId();
        Roles role = rolesRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy role ID: " + roleId));

        Users user = Users.builder()
                .username(request.getUsername())
                // ✅ PRODUCTION — Bỏ comment dòng dưới khi deploy thật
                // .password(passwordEncoder.encode(request.getPassword()))
                // 🚧 TESTING — Lưu plain text để test
                .password(request.getPassword())
                .email(request.getEmail())
                .role(role)
                .createdAt(LocalDate.now())
                .build();
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public JwtResponse buildJwtResponse(String username, String token) {
        Users user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user: " + username));
        return JwtResponse.builder()
                .token(token)
                .type("Bearer")
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().getRoleName())
                .build();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        userRepository.delete(findById(id));
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private Users findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user ID: " + id));
    }

    // Lấy role USER mặc định (giả sử roleName = "USER")
    private Long getDefaultRoleId() {
        return rolesRepository.findByRoleName("USER")
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy role mặc định USER"))
                .getId();
    }

    private UserResponse toResponse(Users u) {
        return UserResponse.builder()
                .id(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .roleName(u.getRole() != null ? u.getRole().getRoleName() : null)
                .createdAt(u.getCreatedAt())
                .build();
    }
}