package vn.intracom.chuongtrinhdaotao.repository;


import vn.intracom.chuongtrinhdaotao.entity.Nganh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NganhRepository extends JpaRepository<Nganh, Long> {

    Optional<Nganh> findByMaNganh(String maNganh);
    List<Nganh> findByTrangThai(Boolean trangThai);
    boolean existsByMaNganh(String maNganh);
}
