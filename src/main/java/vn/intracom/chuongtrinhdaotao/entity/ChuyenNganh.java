package vn.intracom.chuongtrinhdaotao.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ChuyenNganh")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ChuyenNganh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_chuyen_nganh", length = 100)
    private String tenChuyenNganh;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nganh_id")
    private Nganh nganh;

    @Column(name = "mo_ta", columnDefinition = "nvarchar(MAX)")
    private String moTa;

    @Column(name = "trang_thai")
    private Boolean trangThai;
}
