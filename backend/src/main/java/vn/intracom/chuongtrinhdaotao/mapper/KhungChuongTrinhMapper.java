package vn.intracom.chuongtrinhdaotao.mapper;

import org.springframework.stereotype.Component;
import vn.intracom.chuongtrinhdaotao.dto.request.KhungChuongTrinhRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.KhungChuongTrinhResponse;
import vn.intracom.chuongtrinhdaotao.entity.ChuongTrinhDaoTao;
import vn.intracom.chuongtrinhdaotao.entity.KhungChuongTrinh;
import vn.intracom.chuongtrinhdaotao.entity.MonHoc;

@Component
public class KhungChuongTrinhMapper {

    public KhungChuongTrinh toEntity(KhungChuongTrinhRequest request,
                                     ChuongTrinhDaoTao chuongTrinh,
                                     MonHoc monHoc) {
        return KhungChuongTrinh.builder()
                .chuongTrinhDaoTao(chuongTrinh)
                .monHoc(monHoc)
                .hocKy(request.getHocKy())
                .nhomKienThuc(request.getNhomKienThuc())
                .loaiMon(request.getLoaiMon())
                .thuTu(request.getThuTu())
                .ghiChu(request.getGhiChu())
                .build();
    }

    public void updateEntity(KhungChuongTrinh entity, KhungChuongTrinhRequest request,
                             MonHoc monHoc) {
        entity.setMonHoc(monHoc);
        entity.setHocKy(request.getHocKy());
        entity.setNhomKienThuc(request.getNhomKienThuc());
        entity.setLoaiMon(request.getLoaiMon());
        entity.setThuTu(request.getThuTu());
        entity.setGhiChu(request.getGhiChu());
    }

    public KhungChuongTrinhResponse toResponse(KhungChuongTrinh entity) {
        MonHoc m = entity.getMonHoc();
        ChuongTrinhDaoTao ct = entity.getChuongTrinhDaoTao();
        return KhungChuongTrinhResponse.builder()
                .id(entity.getId())
                .chuongTrinhId(ct != null ? ct.getId() : null)
                .tenChuongTrinh(ct != null ? ct.getTenChuongTrinh() : null)
                .monHocId(m != null ? m.getId() : null)
                .maMon(m != null ? m.getMaMon() : null)
                .tenMon(m != null ? m.getTenMon() : null)
                .tinChi(m != null ? m.getTinChi() : null)
                .hocKy(entity.getHocKy())
                .nhomKienThuc(entity.getNhomKienThuc())
                .loaiMon(entity.getLoaiMon())
                .thuTu(entity.getThuTu())
                .ghiChu(entity.getGhiChu())
                .build();
    }
}