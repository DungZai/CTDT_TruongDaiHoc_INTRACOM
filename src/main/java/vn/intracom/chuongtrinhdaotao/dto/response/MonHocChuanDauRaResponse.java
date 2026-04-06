package vn.intracom.chuongtrinhdaotao.dto.response;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MonHocChuanDauRaResponse {
    private Long   id;
    private Long   monHocId;
    private String maMon;
    private String tenMon;
    private Long   chuanDauRaId;
    private String maChuan;
    private String noiDung;
    private String mucDo;
}   