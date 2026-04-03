package vn.intracom.chuongtrinhdaotao.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class HeDaoTaoRequest {

    @NotBlank(message = "Tên hệ không được để trống")
    @Size(max = 100, message = "Tên hệ tối đa 100 ký tự")
    private String tenHe;

    @NotNull(message = "Thời gian đào tạo không được để trống")
    @Min(value = 1, message = "Thời gian đào tạo tối thiểu 1 năm")
    private Integer thoiGianDaoTao;

    @NotNull(message = "Tổng tín chỉ mặc định không được để trống")
    @Min(value = 1, message = "Tổng tín chỉ tối thiểu 1")
    private Integer tongTinChiMacDinh;

    private String moTa;
}