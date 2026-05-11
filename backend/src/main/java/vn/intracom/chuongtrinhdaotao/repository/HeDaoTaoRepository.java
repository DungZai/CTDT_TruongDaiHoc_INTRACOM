package vn.intracom.chuongtrinhdaotao.repository;


import vn.intracom.chuongtrinhdaotao.entity.HeDaoTao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HeDaoTaoRepository extends JpaRepository<HeDaoTao, Long> {

    List<HeDaoTao> findByThoiGianDaoTao(Integer thoiGianDaoTao);
    boolean existsByTenHe(String tenHe);
}