package vn.intracom.chuongtrinhdaotao.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "HeDaoTao")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class HeDaoTao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_he", length = 100)
    private String tenHe;

    @Column(name = "thoi_gian_dao_tao")
    private Integer thoiGianDaoTao;

    @Column(name = "tong_tin_chi_mac_dinh")
    private Integer tongTinChiMacDinh;

    @Column(name = "mo_ta", columnDefinition = "nvarchar(MAX)")
    private String moTa;
}