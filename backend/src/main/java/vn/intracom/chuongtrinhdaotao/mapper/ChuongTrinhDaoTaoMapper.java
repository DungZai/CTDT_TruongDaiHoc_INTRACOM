package vn.intracom.chuongtrinhdaotao.mapper;

import org.springframework.stereotype.Component;
import vn.intracom.chuongtrinhdaotao.dto.request.ChuongTrinhDaoTaoRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.ChuongTrinhDaoTaoResponse;
import vn.intracom.chuongtrinhdaotao.entity.ChuongTrinhDaoTao;
import vn.intracom.chuongtrinhdaotao.entity.HeDaoTao;
import vn.intracom.chuongtrinhdaotao.entity.Nganh;

@Component
public class ChuongTrinhDaoTaoMapper {

    public ChuongTrinhDaoTao toEntity(ChuongTrinhDaoTaoRequest request,
                                      Nganh nganh, HeDaoTao heDaoTao) {
        return ChuongTrinhDaoTao.builder()
                .tenChuongTrinh(request.getTenChuongTrinh())
                .nganh(nganh)
                .heDaoTao(heDaoTao)
                .tongTinChi(request.getTongTinChi())
                .namPhatHanh(request.getNamPhatHanh())
                .moTa(request.getMoTa())
                .trangThai(request.getTrangThai())
                .build();
    }

    public void updateEntity(ChuongTrinhDaoTao entity, ChuongTrinhDaoTaoRequest request,
                             Nganh nganh, HeDaoTao heDaoTao) {
        entity.setTenChuongTrinh(request.getTenChuongTrinh());
        entity.setNganh(nganh);
        entity.setHeDaoTao(heDaoTao);
        entity.setTongTinChi(request.getTongTinChi());
        entity.setNamPhatHanh(request.getNamPhatHanh());
        entity.setMoTa(request.getMoTa());
        entity.setTrangThai(request.getTrangThai());
    }

    public ChuongTrinhDaoTaoResponse toResponse(ChuongTrinhDaoTao entity) {
        return ChuongTrinhDaoTaoResponse.builder()
                .id(entity.getId())
                .tenChuongTrinh(entity.getTenChuongTrinh())
                .nganhId(entity.getNganh() != null ? entity.getNganh().getId() : null)
                .tenNganh(entity.getNganh() != null ? entity.getNganh().getTenNganh() : null)
                .heId(entity.getHeDaoTao() != null ? entity.getHeDaoTao().getId() : null)
                .tenHe(entity.getHeDaoTao() != null ? entity.getHeDaoTao().getTenHe() : null)
                .tongTinChi(entity.getTongTinChi())
                .namPhatHanh(entity.getNamPhatHanh())
                .moTa(entity.getMoTa())
                .trangThai(entity.getTrangThai())
                .build();
    }
}