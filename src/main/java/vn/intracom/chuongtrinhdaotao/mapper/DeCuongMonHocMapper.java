package vn.intracom.chuongtrinhdaotao.mapper;

import org.springframework.stereotype.Component;
import vn.intracom.chuongtrinhdaotao.dto.request.DeCuongMonHocRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.DeCuongMonHocResponse;
import vn.intracom.chuongtrinhdaotao.entity.DeCuongMonHoc;
import vn.intracom.chuongtrinhdaotao.entity.MonHoc;

@Component
public class DeCuongMonHocMapper {

    public DeCuongMonHoc toEntity(DeCuongMonHocRequest request, MonHoc monHoc) {
        return DeCuongMonHoc.builder()
                .monHoc(monHoc)
                .mucTieu(request.getMucTieu())
                .noiDung(request.getNoiDung())
                .phuongPhapDay(request.getPhuongPhapDay())
                .phuongPhapDanhGia(request.getPhuongPhapDanhGia())
                .taiLieu(request.getTaiLieu())
                .version(request.getVersion())
                .build();
    }

    public void updateEntity(DeCuongMonHoc entity, DeCuongMonHocRequest request) {
        entity.setMucTieu(request.getMucTieu());
        entity.setNoiDung(request.getNoiDung());
        entity.setPhuongPhapDay(request.getPhuongPhapDay());
        entity.setPhuongPhapDanhGia(request.getPhuongPhapDanhGia());
        entity.setTaiLieu(request.getTaiLieu());
        entity.setVersion(request.getVersion());
    }

    public DeCuongMonHocResponse toResponse(DeCuongMonHoc entity) {
        MonHoc m = entity.getMonHoc();
        return DeCuongMonHocResponse.builder()
                .id(entity.getId())
                .monHocId(m != null ? m.getId() : null)
                .maMon(m != null ? m.getMaMon() : null)
                .tenMon(m != null ? m.getTenMon() : null)
                .mucTieu(entity.getMucTieu())
                .noiDung(entity.getNoiDung())
                .phuongPhapDay(entity.getPhuongPhapDay())
                .phuongPhapDanhGia(entity.getPhuongPhapDanhGia())
                .taiLieu(entity.getTaiLieu())
                .version(entity.getVersion())
                .build();
    }
}