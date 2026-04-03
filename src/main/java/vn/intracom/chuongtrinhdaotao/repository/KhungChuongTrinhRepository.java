package vn.intracom.chuongtrinhdaotao.repository;

import vn.intracom.chuongtrinhdaotao.entity.KhungChuongTrinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KhungChuongTrinhRepository extends JpaRepository<KhungChuongTrinh, Long> {

    // Lấy toàn bộ khung theo chương trình, sắp xếp theo học kỳ và thứ tự
    List<KhungChuongTrinh> findByChuongTrinhDaoTao_IdOrderByHocKyAscThuTuAsc(Long chuongTrinhId);

    // Lấy môn học theo học kỳ cụ thể trong một chương trình
    List<KhungChuongTrinh> findByChuongTrinhDaoTao_IdAndHocKy(Long chuongTrinhId, Integer hocKy);

    // Lấy theo nhóm kiến thức (ví dụ: Đại cương, Cơ sở ngành, Chuyên ngành)
    List<KhungChuongTrinh> findByChuongTrinhDaoTao_IdAndNhomKienThuc(Long chuongTrinhId, String nhomKienThuc);

    // Lấy theo loại môn (Bắt buộc / Tự chọn)
    List<KhungChuongTrinh> findByChuongTrinhDaoTao_IdAndLoaiMon(Long chuongTrinhId, String loaiMon);

    // Kiểm tra môn học đã có trong khung chương trình chưa
    boolean existsByChuongTrinhDaoTao_IdAndMonHoc_Id(Long chuongTrinhId, Long monHocId);

    // Tổng tín chỉ theo chương trình
    @Query("""
            SELECT SUM(m.tinChi) FROM KhungChuongTrinh k
            JOIN k.monHoc m
            WHERE k.chuongTrinhDaoTao.id = :chuongTrinhId
            """)
    Integer sumTinChiByChuongTrinh(@Param("chuongTrinhId") Long chuongTrinhId);
}