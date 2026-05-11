package vn.intracom.chuongtrinhdaotao.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "MonHoc_ChuanDauRa")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MonHocChuanDauRa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mon_hoc_id")
    private MonHoc monHoc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chuan_dau_ra_id")
    private ChuanDauRa chuanDauRa;

    @Column(name = "muc_do", length = 60)
    private String mucDo;
}