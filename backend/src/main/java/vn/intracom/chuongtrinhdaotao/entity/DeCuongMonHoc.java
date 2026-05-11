package vn.intracom.chuongtrinhdaotao.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "DeCuongMonHoc")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DeCuongMonHoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mon_hoc_id")
    private MonHoc monHoc;

    @Column(name = "muc_tieu", length = 80)
    private String mucTieu;

    @Column(name = "noi_dung", length = 60)
    private String noiDung;

    @Column(name = "phuong_phap_day", length = 60)
    private String phuongPhapDay;

    @Column(name = "phuong_phap_danh_gia", length = 40)
    private String phuongPhapDanhGia;

    @Column(name = "tai_lieu", length = 100)
    private String taiLieu;

    @Column(name = "version", length = 60)
    private String version;
}
