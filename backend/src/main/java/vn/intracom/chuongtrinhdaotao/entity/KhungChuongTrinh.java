package vn.intracom.chuongtrinhdaotao.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "KhungChuongTrinh")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class KhungChuongTrinh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chuong_trinh_id")
    private ChuongTrinhDaoTao chuongTrinhDaoTao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mon_hoc_id")
    private MonHoc monHoc;

    @Column(name = "hoc_ky")
    private Integer hocKy;

    @Column(name = "nhom_kien_thuc", length = 60)
    private String nhomKienThuc;

    @Column(name = "loai_mon", length = 30)
    private String loaiMon;

    @Column(name = "thu_tu")
    private Integer thuTu;

    @Column(name = "ghi_chu", columnDefinition = "nvarchar(MAX)")
    private String ghiChu;
}
