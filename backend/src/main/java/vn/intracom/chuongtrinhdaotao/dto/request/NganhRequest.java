package vn.intracom.chuongtrinhdaotao.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class NganhRequest {

    @NotBlank(message = "Mã ngành không được để trống")
    @Size(max = 20, message = "Mã ngành tối đa 20 ký tự")
    private String maNganh;

    @NotBlank(message = "Tên ngành không được để trống")
    @Size(max = 100, message = "Tên ngành tối đa 100 ký tự")
    private String tenNganh;

    private String moTa;

    @Builder.Default  
    private Boolean trangThai = true;
}