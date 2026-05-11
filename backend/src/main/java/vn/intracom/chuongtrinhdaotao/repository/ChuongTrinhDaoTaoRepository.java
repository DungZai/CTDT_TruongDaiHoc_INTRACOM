package vn.intracom.chuongtrinhdaotao.repository;

import vn.intracom.chuongtrinhdaotao.entity.ChuongTrinhDaoTao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChuongTrinhDaoTaoRepository extends JpaRepository<ChuongTrinhDaoTao, Long> {

    List<ChuongTrinhDaoTao> findByNganh_Id(Long nganhId);
    List<ChuongTrinhDaoTao> findByHeDaoTao_Id(Long heId);
    List<ChuongTrinhDaoTao> findByNganh_IdAndHeDaoTao_Id(Long nganhId, Long heId);
    List<ChuongTrinhDaoTao> findByTrangThai(Boolean trangThai);
    List<ChuongTrinhDaoTao> findByNamPhatHanh(Integer namPhatHanh);

    @Query("SELECT c FROM ChuongTrinhDaoTao c WHERE c.tenChuongTrinh LIKE %:keyword%")
    List<ChuongTrinhDaoTao> searchByTen(@Param("keyword") String keyword);

    boolean existsByTenChuongTrinhAndNganh_IdAndHeDaoTao_IdAndNamPhatHanh(
    String tenChuongTrinh, Long nganhId, Long heId, Integer namPhatHanh
    );
    boolean existsByTenChuongTrinhAndNganh_IdAndHeDaoTao_IdAndNamPhatHanhAndIdNot(
    String tenChuongTrinh, Long nganhId, Long heId, Integer namPhatHanh, Long id
    );
}
