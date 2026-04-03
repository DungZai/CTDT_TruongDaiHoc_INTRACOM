package vn.intracom.chuongtrinhdaotao.dto.response;


import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DeCuongMonHocResponse {

    private Long id;
    private Long monHocId;
    private String maMon;
    private String tenMon;
    private String mucTieu;
    private String noiDung;
    private String phuongPhapDay;
    private String phuongPhapDanhGia;
    private String taiLieu;
    private String version;
}
