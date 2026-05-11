package vn.intracom.chuongtrinhdaotao.repository;

import vn.intracom.chuongtrinhdaotao.entity.MonHocChuanDauRa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonHocChuanDauRaRepository extends JpaRepository<MonHocChuanDauRa, Long> {

    List<MonHocChuanDauRa> findByMonHoc_Id(Long monHocId);
    List<MonHocChuanDauRa> findByChuanDauRa_Id(Long chuanDauRaId);
    boolean existsByMonHoc_IdAndChuanDauRa_Id(Long monHocId, Long chuanDauRaId);
    void deleteByMonHoc_Id(Long monHocId);
    void deleteByMonHoc_IdAndChuanDauRa_Id(Long monHocId, Long chuanDauRaId);
}