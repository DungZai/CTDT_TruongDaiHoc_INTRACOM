package vn.intracom.chuongtrinhdaotao.repository;
import vn.intracom.chuongtrinhdaotao.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {


    @Query("SELECT u FROM Users u WHERE " +
       "(:keyword IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%',:keyword,'%')) " +
       "OR LOWER(u.email) LIKE LOWER(CONCAT('%',:keyword,'%')))")
    Page<Users> search(@Param("keyword") String keyword, Pageable pageable);

    // Dùng cho Spring Security loadUserByUsername
    Optional<Users> findByUsername(String username);

    // Tìm theo email (dùng khi quên mật khẩu hoặc đăng ký)
    Optional<Users> findByEmail(String email);

    // Kiểm tra username / email đã tồn tại chưa
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}