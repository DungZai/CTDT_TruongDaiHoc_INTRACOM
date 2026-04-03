package vn.intracom.chuongtrinhdaotao.repository;


import vn.intracom.chuongtrinhdaotao.entity.Nganh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NganhRepository extends JpaRepository<Nganh, Long> {

    // Tìm theo mã ngành
    Optional<Nganh> findByMaNganh(String maNganh);

    // Lấy danh sách ngành đang hoạt động
    List<Nganh> findByTrangThai(Boolean trangThai);

    // Kiểm tra mã ngành đã tồn tại chưa
    boolean existsByMaNganh(String maNganh);
}
