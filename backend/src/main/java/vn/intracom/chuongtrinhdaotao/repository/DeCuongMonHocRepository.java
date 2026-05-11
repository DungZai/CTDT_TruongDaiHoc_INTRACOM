package vn.intracom.chuongtrinhdaotao.repository;

import vn.intracom.chuongtrinhdaotao.entity.DeCuongMonHoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeCuongMonHocRepository extends JpaRepository<DeCuongMonHoc, Long> {

    Optional<DeCuongMonHoc> findByMonHoc_Id(Long monHocId);
    boolean existsByMonHoc_Id(Long monHocId);
    List<DeCuongMonHoc> findByVersion(String version);
}
