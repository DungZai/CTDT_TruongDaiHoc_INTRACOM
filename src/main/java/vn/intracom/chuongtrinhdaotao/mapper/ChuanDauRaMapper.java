package vn.intracom.chuongtrinhdaotao.mapper;

import org.springframework.stereotype.Component;
import vn.intracom.chuongtrinhdaotao.dto.request.ChuanDauRaRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.ChuanDauRaResponse;
import vn.intracom.chuongtrinhdaotao.entity.ChuanDauRa;
import vn.intracom.chuongtrinhdaotao.entity.ChuongTrinhDaoTao;

@Component
public class ChuanDauRaMapper {

    public ChuanDauRa toEntity(ChuanDauRaRequest request, ChuongTrinhDaoTao chuongTrinh) {
        return ChuanDauRa.builder()
                .chuongTrinhDaoTao(chuongTrinh)
                .maChuan(request.getMaChuan())
                .noiDung(request.getNoiDung())
                .build();
    }

    public void updateEntity(ChuanDauRa entity, ChuanDauRaRequest request) {
        entity.setMaChuan(request.getMaChuan());
        entity.setNoiDung(request.getNoiDung());
    }

    public ChuanDauRaResponse toResponse(ChuanDauRa entity) {
        ChuongTrinhDaoTao ct = entity.getChuongTrinhDaoTao();
        return ChuanDauRaResponse.builder()
                .id(entity.getId())
                .chuongTrinhId(ct != null ? ct.getId() : null)
                .tenChuongTrinh(ct != null ? ct.getTenChuongTrinh() : null)
                .maChuan(entity.getMaChuan())
                .noiDung(entity.getNoiDung())
                .build();
    }
}