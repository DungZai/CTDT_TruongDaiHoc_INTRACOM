package vn.intracom.chuongtrinhdaotao.dto.response;


import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ChuyenNganhResponse {

    private Long id;
    private String tenChuyenNganh;
    private Long nganhId;
    private String tenNganh;
    private String moTa;
    private Boolean trangThai;
}