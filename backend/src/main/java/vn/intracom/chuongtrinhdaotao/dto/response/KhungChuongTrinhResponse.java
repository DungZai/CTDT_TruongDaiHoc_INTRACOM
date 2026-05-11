package vn.intracom.chuongtrinhdaotao.dto.response;


import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class KhungChuongTrinhResponse {

    private Long id;
    private Long chuongTrinhId;
    private String tenChuongTrinh;

    private Long monHocId;
    private String maMon;
    private String tenMon;
    private Integer tinChi;

    private Integer hocKy;
    private String nhomKienThuc;
    private String loaiMon;
    private Integer thuTu;
    private String ghiChu;
}
