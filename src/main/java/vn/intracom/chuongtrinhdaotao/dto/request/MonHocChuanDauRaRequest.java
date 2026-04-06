package vn.intracom.chuongtrinhdaotao.dto.request;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MonHocChuanDauRaRequest {
    private Long   monHocId;
    private Long   chuanDauRaId;
    private String mucDo;
}