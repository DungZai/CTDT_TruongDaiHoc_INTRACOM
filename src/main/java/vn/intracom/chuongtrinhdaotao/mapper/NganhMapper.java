package vn.intracom.chuongtrinhdaotao.mapper;

import org.springframework.stereotype.Component;
import vn.intracom.chuongtrinhdaotao.dto.request.NganhRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.NganhResponse;
import vn.intracom.chuongtrinhdaotao.entity.Nganh;

@Component
public class NganhMapper {

    // Request -> Entity (dùng khi tạo mới)
    public Nganh toEntity(NganhRequest request) {
        return Nganh.builder()
                .maNganh(request.getMaNganh())
                .tenNganh(request.getTenNganh())
                .moTa(request.getMoTa())
                .trangThai(request.getTrangThai())
                .build();
    }

    // Cập nhật entity từ request (dùng khi update — giữ nguyên id)
    public void updateEntity(Nganh nganh, NganhRequest request) {
        nganh.setMaNganh(request.getMaNganh());
        nganh.setTenNganh(request.getTenNganh());
        nganh.setMoTa(request.getMoTa());
        nganh.setTrangThai(request.getTrangThai());
    }

    // Entity -> Response
    public NganhResponse toResponse(Nganh nganh) {
        return NganhResponse.builder()
                .id(nganh.getId())
                .maNganh(nganh.getMaNganh())
                .tenNganh(nganh.getTenNganh())
                .moTa(nganh.getMoTa())
                .trangThai(nganh.getTrangThai())
                .build();
    }
}