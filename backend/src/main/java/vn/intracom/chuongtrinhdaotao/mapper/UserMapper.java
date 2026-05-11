package vn.intracom.chuongtrinhdaotao.mapper;

import org.springframework.stereotype.Component;
import vn.intracom.chuongtrinhdaotao.dto.response.JwtResponse;
import vn.intracom.chuongtrinhdaotao.dto.response.UserResponse;
import vn.intracom.chuongtrinhdaotao.entity.Users;

@Component
public class UserMapper {

    public UserResponse toResponse(Users entity) {
        return UserResponse.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .roleName(entity.getRole() != null ? entity.getRole().getRoleName() : null)
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public JwtResponse toJwtResponse(Users entity, String token) {
        return JwtResponse.builder()
                .token(token)
                .type("Bearer")
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .role(entity.getRole() != null ? entity.getRole().getRoleName() : null)
                .build();
    }
}