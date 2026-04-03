package vn.intracom.chuongtrinhdaotao.dto.response;



import lombok.*;

// Dùng khi nhúng môn học vào response khác (tránh lồng vô hạn)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MonHocSimpleResponse {

    private Long id;
    private String maMon;
    private String tenMon;
    private Integer tinChi;
}