package vn.intracom.chuongtrinhdaotao.repository;

import vn.intracom.chuongtrinhdaotao.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

    // Dùng cho Spring Security loadUserByUsername
    Optional<Users> findByUsername(String username);

    // Tìm theo email (dùng khi quên mật khẩu hoặc đăng ký)
    Optional<Users> findByEmail(String email);

    // Kiểm tra username / email đã tồn tại chưa
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}