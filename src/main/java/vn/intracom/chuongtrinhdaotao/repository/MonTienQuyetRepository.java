package vn.intracom.chuongtrinhdaotao.repository;

import vn.intracom.chuongtrinhdaotao.entity.MonTienQuyet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonTienQuyetRepository extends JpaRepository<MonTienQuyet, Long> {

    // Lấy danh sách môn tiên quyết của một môn học
    List<MonTienQuyet> findByMonHoc_Id(Long monHocId);

    // Kiểm tra liên kết đã tồn tại chưa (tránh duplicate)
    boolean existsByMonHoc_IdAndMonTienQuyet_Id(Long monHocId, Long monTienQuyetId);

    // Xóa toàn bộ tiên quyết của một môn
    void deleteByMonHoc_Id(Long monHocId);
}