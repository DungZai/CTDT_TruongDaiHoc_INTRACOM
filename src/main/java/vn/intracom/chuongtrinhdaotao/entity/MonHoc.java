package vn.intracom.chuongtrinhdaotao.entity;


import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "MonHoc")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MonHoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_mon", length = 20)
    private String maMon;

    @Column(name = "ten_mon", length = 400)
    private String tenMon;

    @Column(name = "tin_chi")
    private Integer tinChi;

    @Column(name = "so_tiet_lt")
    private Integer soTietLt;

    @Column(name = "so_tiet_th")
    private Integer soTietTh;

    @Column(name = "mo_ta", columnDefinition = "nvarchar(MAX)")
    private String moTa;

    @Column(name = "trang_thai")
    private Boolean trangThai;

    @OneToMany(mappedBy = "monHoc", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MonTienQuyet> danhSachTienQuyet;
}