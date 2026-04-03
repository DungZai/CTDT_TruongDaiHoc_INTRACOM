package vn.intracom.chuongtrinhdaotao.repository;

import vn.intracom.chuongtrinhdaotao.entity.ChuanDauRa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChuanDauRaRepository extends JpaRepository<ChuanDauRa, Long> {

    // Lấy tất cả chuẩn đầu ra của một chương trình
    List<ChuanDauRa> findByChuongTrinhDaoTao_Id(Long chuongTrinhId);

    // Tìm theo mã chuẩn trong một chương trình
    Optional<ChuanDauRa> findByChuongTrinhDaoTao_IdAndMaChuan(Long chuongTrinhId, String maChuan);

    // Kiểm tra mã chuẩn đã tồn tại trong chương trình chưa
    boolean existsByChuongTrinhDaoTao_IdAndMaChuan(Long chuongTrinhId, String maChuan);
}