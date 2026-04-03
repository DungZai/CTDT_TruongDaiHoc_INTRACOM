package vn.intracom.chuongtrinhdaotao.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ChuongTrinhDaoTaoRequest {

    @NotBlank(message = "Tên chương trình không được để trống")
    @Size(max = 100, message = "Tên chương trình tối đa 100 ký tự")
    private String tenChuongTrinh;

    @NotNull(message = "Ngành không được để trống")
    private Long nganhId;

    @NotNull(message = "Hệ đào tạo không được để trống")
    private Long heId;

    @Min(value = 1, message = "Tổng tín chỉ tối thiểu 1")
    private Integer tongTinChi;

    private Integer namPhatHanh;

    private String moTa;

    private Boolean trangThai = true;
}
