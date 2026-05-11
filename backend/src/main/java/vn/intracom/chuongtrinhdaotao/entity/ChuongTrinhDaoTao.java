package vn.intracom.chuongtrinhdaotao.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ChuongTrinhDaoTao")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ChuongTrinhDaoTao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_chuong_trinh", length = 100)
    private String tenChuongTrinh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nganh_id")
    private Nganh nganh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "he_id")
    private HeDaoTao heDaoTao;

    @Column(name = "tong_tin_chi")
    private Integer tongTinChi;

    @Column(name = "nam_phat_hanh")
    private Integer namPhatHanh;

    @Column(name = "mo_ta", columnDefinition = "nvarchar(MAX)")
    private String moTa;

    @Column(name = "tang_thai")
    private Boolean trangThai;
}