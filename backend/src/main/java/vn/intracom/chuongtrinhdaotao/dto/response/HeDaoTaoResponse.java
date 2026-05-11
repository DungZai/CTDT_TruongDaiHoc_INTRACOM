package vn.intracom.chuongtrinhdaotao.dto.response;


import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class HeDaoTaoResponse {

    private Long id;
    private String tenHe;
    private Integer thoiGianDaoTao;
    private Integer tongTinChiMacDinh;
    private String moTa;
}
