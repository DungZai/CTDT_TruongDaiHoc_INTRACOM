package vn.intracom.chuongtrinhdaotao.repository;

import vn.intracom.chuongtrinhdaotao.entity.MonHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MonHocRepository extends JpaRepository<MonHoc, Long> {

    // Tìm theo mã môn
    Optional<MonHoc> findByMaMon(String maMon);

    // Kiểm tra mã môn đã tồn tại chưa
    boolean existsByMaMon(String maMon);

    // Lấy môn học đang hoạt động
    List<MonHoc> findByTrangThai(Boolean trangThai);

    // Tìm kiếm theo tên môn (LIKE)
    @Query("SELECT m FROM MonHoc m WHERE m.tenMon LIKE %:keyword%")
    List<MonHoc> searchByTenMon(@Param("keyword") String keyword);

    // Lấy danh sách môn theo số tín chỉ
    List<MonHoc> findByTinChi(Integer tinChi);

    // Lấy tất cả môn trong một khung chương trình
    @Query("""
            SELECT m FROM MonHoc m
            JOIN KhungChuongTrinh k ON k.monHoc.id = m.id
            WHERE k.chuongTrinhDaoTao.id = :chuongTrinhId
            ORDER BY k.hocKy, k.thuTu
            """)
    List<MonHoc> findByChuongTrinh(@Param("chuongTrinhId") Long chuongTrinhId);
}