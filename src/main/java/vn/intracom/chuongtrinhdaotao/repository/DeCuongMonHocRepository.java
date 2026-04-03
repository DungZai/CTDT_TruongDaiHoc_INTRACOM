package vn.intracom.chuongtrinhdaotao.repository;

import vn.intracom.chuongtrinhdaotao.entity.DeCuongMonHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeCuongMonHocRepository extends JpaRepository<DeCuongMonHoc, Long> {

    // Lấy đề cương theo môn học
    Optional<DeCuongMonHoc> findByMonHoc_Id(Long monHocId);

    // Kiểm tra môn học đã có đề cương chưa
    boolean existsByMonHoc_Id(Long monHocId);

    // Lấy danh sách đề cương theo version
    List<DeCuongMonHoc> findByVersion(String version);
}
