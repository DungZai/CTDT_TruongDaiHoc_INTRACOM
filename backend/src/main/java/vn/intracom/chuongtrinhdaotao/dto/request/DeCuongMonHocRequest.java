package vn.intracom.chuongtrinhdaotao.dto.request;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class DeCuongMonHocRequest {

    @NotNull(message = "Môn học không được để trống")
    private Long monHocId;

    @Size(max = 80, message = "Mục tiêu tối đa 80 ký tự")
    private String mucTieu;

    @Size(max = 60, message = "Nội dung tối đa 60 ký tự")
    private String noiDung;

    @Size(max = 60, message = "Phương pháp dạy tối đa 60 ký tự")
    private String phuongPhapDay;

    @Size(max = 40, message = "Phương pháp đánh giá tối đa 40 ký tự")
    private String phuongPhapDanhGia;

    @Size(max = 100, message = "Tài liệu tối đa 100 ký tự")
    private String taiLieu;

    @Size(max = 60, message = "Version tối đa 60 ký tự")
    private String version;
}