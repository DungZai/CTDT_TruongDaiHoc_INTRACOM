package vn.intracom.chuongtrinhdaotao.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.intracom.chuongtrinhdaotao.dto.request.RegisterRequest;
import vn.intracom.chuongtrinhdaotao.dto.request.UserUpdateRequest;
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

    private final UserRepository  userRepository;
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
        if (userRepository.existsByUsername(request.getUsername()))
            throw new BadRequestException("Username '" + request.getUsername() + "' đã tồn tại");
        if (userRepository.existsByEmail(request.getEmail()))
            throw new BadRequestException("Email '" + request.getEmail() + "' đã được sử dụng");

        Long roleId = request.getRoleId() != null ? request.getRoleId() : getDefaultRoleId();
        Roles role  = rolesRepository.findById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy role ID: " + roleId));

        Users user = Users.builder()
                .username(request.getUsername())
                .password(request.getPassword()) // TODO: encode khi deploy
                .email(request.getEmail())
                .role(role)
                .createdAt(LocalDate.now())
                .build();
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserResponse create(RegisterRequest request) {
        register(request);
        return getByUsername(request.getUsername());
    }

    @Override
    @Transactional
    public UserResponse update(Long id, UserUpdateRequest request) {
        Users user = findById(id);

        if (request.getUsername() != null && !request.getUsername().isBlank()
                && !request.getUsername().equals(user.getUsername())) {
            if (userRepository.existsByUsername(request.getUsername()))
                throw new BadRequestException("Username '" + request.getUsername() + "' đã tồn tại.");
            user.setUsername(request.getUsername());
        }

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            if (!request.getEmail().equals(user.getEmail())
                    && userRepository.existsByEmail(request.getEmail()))
                throw new BadRequestException("Email '" + request.getEmail() + "' đã được sử dụng.");
            user.setEmail(request.getEmail());
        }

        if (request.getRoleId() != null) {
            Roles role = rolesRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Không tìm thấy role ID: " + request.getRoleId()));
            user.setRole(role);
        }

        return toResponse(userRepository.save(user));
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

    // ── Helpers ──────────────────────────────────────────────────────────────

    private Users findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user ID: " + id));
    }

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

    @Override
@Transactional
public void changePassword(String username, String currentPassword, String newPassword) {
    Users user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user: " + username));

    if (!passwordEncoder.matches(currentPassword, user.getPassword()))
        throw new BadRequestException("Mật khẩu hiện tại không đúng");

    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
}

}