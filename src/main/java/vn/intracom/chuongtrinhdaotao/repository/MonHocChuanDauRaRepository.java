package vn.intracom.chuongtrinhdaotao.repository;

import vn.intracom.chuongtrinhdaotao.entity.MonHocChuanDauRa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonHocChuanDauRaRepository extends JpaRepository<MonHocChuanDauRa, Long> {

    // Lấy tất cả chuẩn đầu ra của một môn học
    List<MonHocChuanDauRa> findByMonHoc_Id(Long monHocId);

    // Lấy tất cả môn học đáp ứng một chuẩn đầu ra
    List<MonHocChuanDauRa> findByChuanDauRa_Id(Long chuanDauRaId);

    // Kiểm tra liên kết đã tồn tại chưa
    boolean existsByMonHoc_IdAndChuanDauRa_Id(Long monHocId, Long chuanDauRaId);

    // Xóa toàn bộ liên kết của một môn học
    void deleteByMonHoc_Id(Long monHocId);

    // Xóa toàn bộ liên kết của một chuẩn đầu ra
    void deleteByMonHoc_IdAndChuanDauRa_Id(Long monHocId, Long chuanDauRaId);
}