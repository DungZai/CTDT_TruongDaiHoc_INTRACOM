package vn.intracom.chuongtrinhdaotao.repository;

import vn.intracom.chuongtrinhdaotao.entity.ChuanDauRa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChuanDauRaRepository extends JpaRepository<ChuanDauRa, Long> {
    
    List<ChuanDauRa> findByChuongTrinhDaoTao_Id(Long chuongTrinhId);
    Optional<ChuanDauRa> findByChuongTrinhDaoTao_IdAndMaChuan(Long chuongTrinhId, String maChuan);
    boolean existsByChuongTrinhDaoTao_IdAndMaChuan(Long chuongTrinhId, String maChuan);
}