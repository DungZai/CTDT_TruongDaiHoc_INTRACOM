package vn.intracom.chuongtrinhdaotao.repository;

import vn.intracom.chuongtrinhdaotao.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {

    // Tìm theo tên role — dùng trong UserServiceImpl để lấy role mặc định
    Optional<Roles> findByRoleName(String roleName);

    // Kiểm tra role đã tồn tại chưa
    boolean existsByRoleName(String roleName);

    // Lấy tất cả role (dùng cho màn hình phân quyền)
    List<Roles> findAllByOrderByRoleNameAsc();
}