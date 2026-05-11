package vn.intracom.chuongtrinhdaotao.repository;

import vn.intracom.chuongtrinhdaotao.entity.KhungChuongTrinh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KhungChuongTrinhRepository extends JpaRepository<KhungChuongTrinh, Long> {

    List<KhungChuongTrinh> findByChuongTrinhDaoTao_IdOrderByHocKyAscThuTuAsc(Long chuongTrinhId);
    List<KhungChuongTrinh> findByChuongTrinhDaoTao_IdAndHocKy(Long chuongTrinhId, Integer hocKy);
    List<KhungChuongTrinh> findByChuongTrinhDaoTao_IdAndNhomKienThuc(Long chuongTrinhId, String nhomKienThuc);

    List<KhungChuongTrinh> findByChuongTrinhDaoTao_IdAndLoaiMon(Long chuongTrinhId, String loaiMon);
    boolean existsByChuongTrinhDaoTao_IdAndMonHoc_Id(Long chuongTrinhId, Long monHocId);

    @Query("""
            SELECT SUM(m.tinChi) FROM KhungChuongTrinh k
            JOIN k.monHoc m
            WHERE k.chuongTrinhDaoTao.id = :chuongTrinhId
            """)
    Integer sumTinChiByChuongTrinh(@Param("chuongTrinhId") Long chuongTrinhId);
}