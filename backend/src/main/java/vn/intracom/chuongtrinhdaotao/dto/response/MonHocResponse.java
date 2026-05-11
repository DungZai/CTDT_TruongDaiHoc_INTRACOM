package vn.intracom.chuongtrinhdaotao.dto.response;


import lombok.*;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MonHocResponse {

    private Long id;
    private String maMon;
    private String tenMon;
    private Integer tinChi;
    private Integer soTietLt;
    private Integer soTietTh;
    private String moTa;
    private Boolean trangThai;


    private List<MonHocSimpleResponse> danhSachTienQuyet;
}
