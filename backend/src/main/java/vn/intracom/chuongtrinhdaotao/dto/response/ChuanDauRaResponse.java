package vn.intracom.chuongtrinhdaotao.dto.response;


import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ChuanDauRaResponse {

    private Long id;
    private Long chuongTrinhId;
    private String tenChuongTrinh;
    private String maChuan;
    private String noiDung;
}
