package vn.intracom.chuongtrinhdaotao.dto.response;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ChuongTrinhDaoTaoResponse {

    private Long id;
    private String tenChuongTrinh;
    private Long nganhId;
    private String tenNganh;
    private Long heId;
    private String tenHe;
    private Integer tongTinChi;
    private Integer namPhatHanh;
    private String moTa;
    private Boolean trangThai;
}
