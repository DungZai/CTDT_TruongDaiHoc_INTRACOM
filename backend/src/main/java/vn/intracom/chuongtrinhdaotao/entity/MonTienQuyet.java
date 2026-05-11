package vn.intracom.chuongtrinhdaotao.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "MonTienQuyet")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MonTienQuyet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mon_hoc_id")
    private MonHoc monHoc;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mon_tien_quyet_id")
    private MonHoc monTienQuyet;
}