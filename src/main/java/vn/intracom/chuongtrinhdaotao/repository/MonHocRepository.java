package vn.intracom.chuongtrinhdaotao.repository;
 
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.intracom.chuongtrinhdaotao.entity.MonHoc;
 
import java.util.List;
import java.util.Optional;
 
@Repository
public interface MonHocRepository extends JpaRepository<MonHoc, Long> {
 
    Optional<MonHoc> findByMaMon(String maMon);
 
    boolean existsByMaMon(String maMon);
 
    List<MonHoc> findByTrangThai(Boolean trangThai);
 
    List<MonHoc> findByTinChi(Integer tinChi);
 
    // Tìm kiếm có phân trang (mã môn + tên môn)
    @Query("SELECT m FROM MonHoc m WHERE " +
           "(:keyword IS NULL OR LOWER(m.maMon)  LIKE LOWER(CONCAT('%',:keyword,'%')) " +
           "OR                   LOWER(m.tenMon) LIKE LOWER(CONCAT('%',:keyword,'%')))")
    Page<MonHoc> search(@Param("keyword") String keyword, Pageable pageable);
 
    // Giữ lại tìm kiếm cũ (không phân trang) dùng cho các dropdown
    @Query("SELECT m FROM MonHoc m WHERE m.tenMon LIKE %:keyword%")
    List<MonHoc> searchByTenMon(@Param("keyword") String keyword);
 
    // Lấy tất cả môn trong một khung chương trình
    @Query("""
            SELECT m FROM MonHoc m
            JOIN KhungChuongTrinh k ON k.monHoc.id = m.id
            WHERE k.chuongTrinhDaoTao.id = :chuongTrinhId
            ORDER BY k.hocKy, k.thuTu
            """)
    List<MonHoc> findByChuongTrinh(@Param("chuongTrinhId") Long chuongTrinhId);
}