package vn.intracom.chuongtrinhdaotao.dto.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ChuanDauRaRequest {

    @NotNull(message = "Chương trình đào tạo không được để trống")
    private Long chuongTrinhId;

    @NotBlank(message = "Mã chuẩn không được để trống")
    @Size(max = 40, message = "Mã chuẩn tối đa 40 ký tự")
    private String maChuan;

    private String noiDung;
}
