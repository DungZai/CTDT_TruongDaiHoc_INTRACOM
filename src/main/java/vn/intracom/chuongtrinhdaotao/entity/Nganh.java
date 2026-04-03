package vn.intracom.chuongtrinhdaotao.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Nganh")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Nganh {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_nganh", length = 20)
    private String maNganh;

    @Column(name = "ten_nganh", length = 100)
    private String tenNganh;

    @Column(name = "mo_ta", columnDefinition = "nvarchar(MAX)")
    private String moTa;

    @Column(name = "trang_thai")
    private Boolean trangThai;
}