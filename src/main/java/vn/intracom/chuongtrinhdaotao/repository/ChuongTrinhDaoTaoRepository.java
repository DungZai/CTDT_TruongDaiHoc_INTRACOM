package vn.intracom.chuongtrinhdaotao.repository;

import vn.intracom.chuongtrinhdaotao.entity.ChuongTrinhDaoTao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChuongTrinhDaoTaoRepository extends JpaRepository<ChuongTrinhDaoTao, Long> {

    // Lấy chương trình theo ngành
    List<ChuongTrinhDaoTao> findByNganh_Id(Long nganhId);

    // Lấy chương trình theo hệ đào tạo
    List<ChuongTrinhDaoTao> findByHeDaoTao_Id(Long heId);

    // Lấy chương trình theo ngành + hệ
    List<ChuongTrinhDaoTao> findByNganh_IdAndHeDaoTao_Id(Long nganhId, Long heId);

    // Lấy chương trình đang hoạt động
    List<ChuongTrinhDaoTao> findByTrangThai(Boolean trangThai);

    // Tìm kiếm theo năm phát hành
    List<ChuongTrinhDaoTao> findByNamPhatHanh(Integer namPhatHanh);

    // Tìm kiếm theo tên (LIKE)
    @Query("SELECT c FROM ChuongTrinhDaoTao c WHERE c.tenChuongTrinh LIKE %:keyword%")
    List<ChuongTrinhDaoTao> searchByTen(@Param("keyword") String keyword);

    // ChuongTrinhDaoTaoRepository.java
boolean existsByTenChuongTrinhAndNganh_IdAndHeDaoTao_IdAndNamPhatHanh(
    String tenChuongTrinh, Long nganhId, Long heId, Integer namPhatHanh
);

boolean existsByTenChuongTrinhAndNganh_IdAndHeDaoTao_IdAndNamPhatHanhAndIdNot(
    String tenChuongTrinh, Long nganhId, Long heId, Integer namPhatHanh, Long id
);
}
