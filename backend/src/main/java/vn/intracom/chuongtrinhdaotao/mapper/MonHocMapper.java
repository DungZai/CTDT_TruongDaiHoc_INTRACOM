package vn.intracom.chuongtrinhdaotao.mapper;

import org.springframework.stereotype.Component;
import vn.intracom.chuongtrinhdaotao.dto.request.MonHocRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.MonHocResponse;
import vn.intracom.chuongtrinhdaotao.dto.response.MonHocSimpleResponse;
import vn.intracom.chuongtrinhdaotao.entity.MonHoc;
import vn.intracom.chuongtrinhdaotao.entity.MonTienQuyet;

import java.util.Collections;
import java.util.List;

@Component
public class MonHocMapper {

    public MonHoc toEntity(MonHocRequest request) {
        return MonHoc.builder()
                .maMon(request.getMaMon())
                .tenMon(request.getTenMon())
                .tinChi(request.getTinChi())
                .soTietLt(request.getSoTietLt())
                .soTietTh(request.getSoTietTh())
                .moTa(request.getMoTa())
                .trangThai(request.getTrangThai())
                .build();
    }

    public void updateEntity(MonHoc entity, MonHocRequest request) {
        entity.setMaMon(request.getMaMon());
        entity.setTenMon(request.getTenMon());
        entity.setTinChi(request.getTinChi());
        entity.setSoTietLt(request.getSoTietLt());
        entity.setSoTietTh(request.getSoTietTh());
        entity.setMoTa(request.getMoTa());
        entity.setTrangThai(request.getTrangThai());
    }

    // Chuyển đổi entity sang response, nhận thêm list MonTienQuyet từ service
    public MonHocResponse toResponse(MonHoc entity, List<MonTienQuyet> danhSachTienQuyet) {
        List<MonHocSimpleResponse> tienQuyet = danhSachTienQuyet == null
                ? Collections.emptyList()
                : danhSachTienQuyet.stream()
                        .map(mtq -> toSimpleResponse(mtq.getMonTienQuyet()))
                        .toList();

        return MonHocResponse.builder()
                .id(entity.getId())
                .maMon(entity.getMaMon())
                .tenMon(entity.getTenMon())
                .tinChi(entity.getTinChi())
                .soTietLt(entity.getSoTietLt())
                .soTietTh(entity.getSoTietTh())
                .moTa(entity.getMoTa())
                .trangThai(entity.getTrangThai())
                .danhSachTienQuyet(tienQuyet)
                .build();
    }

    // Simple response — dùng khi nhúng vào response khác
    public MonHocSimpleResponse toSimpleResponse(MonHoc entity) {
        return MonHocSimpleResponse.builder()
                .id(entity.getId())
                .maMon(entity.getMaMon())
                .tenMon(entity.getTenMon())
                .tinChi(entity.getTinChi())
                .build();
    }
}