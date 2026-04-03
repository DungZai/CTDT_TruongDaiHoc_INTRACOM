package vn.intracom.chuongtrinhdaotao.repository;


import vn.intracom.chuongtrinhdaotao.entity.HeDaoTao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeDaoTaoRepository extends JpaRepository<HeDaoTao, Long> {

    // Tìm theo thời gian đào tạo (ví dụ: 4 năm, 2 năm)
    List<HeDaoTao> findByThoiGianDaoTao(Integer thoiGianDaoTao);

    // Kiểm tra tên hệ đã tồn tại chưa
    boolean existsByTenHe(String tenHe);
}