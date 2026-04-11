package vn.intracom.chuongtrinhdaotao.repository;

import vn.intracom.chuongtrinhdaotao.entity.ChuyenNganh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface ChuyenNganhRepository extends JpaRepository<ChuyenNganh, Long> {

    @Query("SELECT c FROM ChuyenNganh c WHERE " +
       "(:keyword IS NULL OR LOWER(c.tenChuyenNganh) LIKE LOWER(CONCAT('%',:keyword,'%'))) " +
       "AND (:nganhId IS NULL OR c.nganh.id = :nganhId)")
Page<ChuyenNganh> search(@Param("keyword") String keyword,
                          @Param("nganhId") Long nganhId,
                          Pageable pageable);
    // Lấy tất cả chuyên ngành thuộc một ngành
    List<ChuyenNganh> findByNganh_Id(Long nganhId);

    // Lấy chuyên ngành đang hoạt động theo ngành
    List<ChuyenNganh> findByNganh_IdAndTrangThai(Long nganhId, Boolean trangThai);

    // Lấy tất cả chuyên ngành đang hoạt động
    List<ChuyenNganh> findByTrangThai(Boolean trangThai);
}