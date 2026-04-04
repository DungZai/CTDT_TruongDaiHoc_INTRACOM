package vn.intracom.chuongtrinhdaotao.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ChuyenNganhRequest {

    @NotBlank(message = "Tên chuyên ngành không được để trống")
    @Size(max = 100, message = "Tên chuyên ngành tối đa 100 ký tự")
    private String tenChuyenNganh;

    @NotNull(message = "Ngành không được để trống")
    private Long nganhId;

    private String moTa;

    @Builder.Default  // ✅
    private Boolean trangThai = true;
}