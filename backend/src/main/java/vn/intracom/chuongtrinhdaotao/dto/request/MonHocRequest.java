package vn.intracom.chuongtrinhdaotao.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class MonHocRequest {

    @NotBlank(message = "Mã môn không được để trống")
    @Size(max = 20, message = "Mã môn tối đa 20 ký tự")
    private String maMon;

    @NotBlank(message = "Tên môn không được để trống")
    @Size(max = 400, message = "Tên môn tối đa 400 ký tự")
    private String tenMon;

    @NotNull(message = "Số tín chỉ không được để trống")
    @Min(value = 1, message = "Số tín chỉ tối thiểu 1")
    private Integer tinChi;

    @Min(value = 0, message = "Số tiết lý thuyết không âm")
    private Integer soTietLt;

    @Min(value = 0, message = "Số tiết thực hành không âm")
    private Integer soTietTh;

    private String moTa;

    @Builder.Default 
    private Boolean trangThai = true;
}