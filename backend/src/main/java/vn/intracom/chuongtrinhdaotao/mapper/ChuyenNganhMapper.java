package vn.intracom.chuongtrinhdaotao.mapper;

import org.springframework.stereotype.Component;
import vn.intracom.chuongtrinhdaotao.dto.request.ChuyenNganhRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.ChuyenNganhResponse;
import vn.intracom.chuongtrinhdaotao.entity.ChuyenNganh;
import vn.intracom.chuongtrinhdaotao.entity.Nganh;

@Component
public class ChuyenNganhMapper {

    // Cần truyền Nganh entity vào vì request chỉ chứa nganhId
    public ChuyenNganh toEntity(ChuyenNganhRequest request, Nganh nganh) {
        return ChuyenNganh.builder()
                .tenChuyenNganh(request.getTenChuyenNganh())
                .nganh(nganh)
                .moTa(request.getMoTa())
                .trangThai(request.getTrangThai())
                .build();
    }

    public void updateEntity(ChuyenNganh entity, ChuyenNganhRequest request, Nganh nganh) {
        entity.setTenChuyenNganh(request.getTenChuyenNganh());
        entity.setNganh(nganh);
        entity.setMoTa(request.getMoTa());
        entity.setTrangThai(request.getTrangThai());
    }

    public ChuyenNganhResponse toResponse(ChuyenNganh entity) {
        return ChuyenNganhResponse.builder()
                .id(entity.getId())
                .tenChuyenNganh(entity.getTenChuyenNganh())
                .nganhId(entity.getNganh() != null ? entity.getNganh().getId() : null)
                .tenNganh(entity.getNganh() != null ? entity.getNganh().getTenNganh() : null)
                .moTa(entity.getMoTa())
                .trangThai(entity.getTrangThai())
                .build();
    }
}