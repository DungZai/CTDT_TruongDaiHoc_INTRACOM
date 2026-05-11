package vn.intracom.chuongtrinhdaotao.dto.request;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class KhungChuongTrinhRequest {

    @NotNull(message = "Chương trình đào tạo không được để trống")
    private Long chuongTrinhId;

    @NotNull(message = "Môn học không được để trống")
    private Long monHocId;

    @NotNull(message = "Học kỳ không được để trống")
    @Min(value = 1, message = "Học kỳ tối thiểu là 1")
    private Integer hocKy;

    @Size(max = 60, message = "Nhóm kiến thức tối đa 60 ký tự")
    private String nhomKienThuc;

    @Size(max = 30, message = "Loại môn tối đa 30 ký tự")
    private String loaiMon;

    private Integer thuTu;

    private String ghiChu;
}
