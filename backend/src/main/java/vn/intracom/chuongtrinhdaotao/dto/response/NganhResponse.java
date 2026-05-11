package vn.intracom.chuongtrinhdaotao.dto.response;


import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class NganhResponse {

    private Long id;
    private String maNganh;
    private String tenNganh;
    private String moTa;
    private Boolean trangThai;
}
