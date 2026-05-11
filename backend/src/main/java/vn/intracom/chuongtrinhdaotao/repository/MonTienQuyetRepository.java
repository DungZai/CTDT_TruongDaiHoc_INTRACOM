package vn.intracom.chuongtrinhdaotao.repository;

import vn.intracom.chuongtrinhdaotao.entity.MonTienQuyet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MonTienQuyetRepository extends JpaRepository<MonTienQuyet, Long> {

    List<MonTienQuyet> findByMonHoc_Id(Long monHocId);
    boolean existsByMonHoc_IdAndMonTienQuyet_Id(Long monHocId, Long monTienQuyetId);
    void deleteByMonHoc_Id(Long monHocId);
}