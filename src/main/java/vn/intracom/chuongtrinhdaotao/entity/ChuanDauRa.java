package vn.intracom.chuongtrinhdaotao.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "ChuanDauRa")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ChuanDauRa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chuong_trinh_id")
    private ChuongTrinhDaoTao chuongTrinhDaoTao;

    @Column(name = "ma_chuan", length = 40)
    private String maChuan;

    @Column(name = "noi_dung", columnDefinition = "nvarchar(MAX)")
    private String noiDung;

    @OneToMany(mappedBy = "chuanDauRa", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MonHocChuanDauRa> danhSachMonHoc;
}