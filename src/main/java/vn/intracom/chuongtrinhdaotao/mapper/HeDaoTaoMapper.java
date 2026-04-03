package vn.intracom.chuongtrinhdaotao.mapper;

import org.springframework.stereotype.Component;
import vn.intracom.chuongtrinhdaotao.dto.request.HeDaoTaoRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.HeDaoTaoResponse;
import vn.intracom.chuongtrinhdaotao.entity.HeDaoTao;

@Component
public class HeDaoTaoMapper {

    public HeDaoTao toEntity(HeDaoTaoRequest request) {
        return HeDaoTao.builder()
                .tenHe(request.getTenHe())
                .thoiGianDaoTao(request.getThoiGianDaoTao())
                .tongTinChiMacDinh(request.getTongTinChiMacDinh())
                .moTa(request.getMoTa())
                .build();
    }

    public void updateEntity(HeDaoTao entity, HeDaoTaoRequest request) {
        entity.setTenHe(request.getTenHe());
        entity.setThoiGianDaoTao(request.getThoiGianDaoTao());
        entity.setTongTinChiMacDinh(request.getTongTinChiMacDinh());
        entity.setMoTa(request.getMoTa());
    }

    public HeDaoTaoResponse toResponse(HeDaoTao entity) {
        return HeDaoTaoResponse.builder()
                .id(entity.getId())
                .tenHe(entity.getTenHe())
                .thoiGianDaoTao(entity.getThoiGianDaoTao())
                .tongTinChiMacDinh(entity.getTongTinChiMacDinh())
                .moTa(entity.getMoTa())
                .build();
    }
}