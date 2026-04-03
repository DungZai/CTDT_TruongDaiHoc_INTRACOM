package vn.intracom.chuongtrinhdaotao.repository;

import vn.intracom.chuongtrinhdaotao.entity.ChuyenNganh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChuyenNganhRepository extends JpaRepository<ChuyenNganh, Long> {

    // Lấy tất cả chuyên ngành thuộc một ngành
    List<ChuyenNganh> findByNganh_Id(Long nganhId);

    // Lấy chuyên ngành đang hoạt động theo ngành
    List<ChuyenNganh> findByNganh_IdAndTrangThai(Long nganhId, Boolean trangThai);

    // Lấy tất cả chuyên ngành đang hoạt động
    List<ChuyenNganh> findByTrangThai(Boolean trangThai);
}