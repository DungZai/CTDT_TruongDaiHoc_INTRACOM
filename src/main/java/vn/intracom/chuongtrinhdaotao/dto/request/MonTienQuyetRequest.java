package vn.intracom.chuongtrinhdaotao.dto.request;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MonTienQuyetRequest {
    private Long monHocId;
    private Long monTienQuyetId;
}