package vn.intracom.chuongtrinhdaotao.dto.response;



import lombok.*;


@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MonHocSimpleResponse {

    private Long id;
    private String maMon;
    private String tenMon;
    private Integer tinChi;
}