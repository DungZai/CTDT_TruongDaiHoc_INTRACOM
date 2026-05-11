package vn.intracom.chuongtrinhdaotao.dto.response;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MonTienQuyetResponse {
    private Long   id;
    private Long   monHocId;
    private String maMon;
    private String tenMon;
    private Long   monTienQuyetId;
    private String maMonTienQuyet;
    private String tenMonTienQuyet;
}