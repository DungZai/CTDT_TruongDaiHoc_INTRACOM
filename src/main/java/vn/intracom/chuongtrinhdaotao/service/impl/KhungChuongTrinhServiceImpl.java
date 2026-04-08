package vn.intracom.chuongtrinhdaotao.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.intracom.chuongtrinhdaotao.dto.request.KhungChuongTrinhRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.KhungChuongTrinhResponse;
import vn.intracom.chuongtrinhdaotao.entity.ChuongTrinhDaoTao;
import vn.intracom.chuongtrinhdaotao.entity.KhungChuongTrinh;
import vn.intracom.chuongtrinhdaotao.entity.MonHoc;
import vn.intracom.chuongtrinhdaotao.exception.BadRequestException;
import vn.intracom.chuongtrinhdaotao.exception.ResourceNotFoundException;
import vn.intracom.chuongtrinhdaotao.repository.ChuongTrinhDaoTaoRepository;
import vn.intracom.chuongtrinhdaotao.repository.KhungChuongTrinhRepository;
import vn.intracom.chuongtrinhdaotao.repository.MonHocRepository;
import vn.intracom.chuongtrinhdaotao.service.IKhungChuongTrinhService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KhungChuongTrinhServiceImpl implements IKhungChuongTrinhService {

    private final KhungChuongTrinhRepository khungRepository;
    private final ChuongTrinhDaoTaoRepository chuongTrinhRepository;
    private final MonHocRepository monHocRepository;

    @Override
    @Transactional(readOnly = true)
    public List<KhungChuongTrinhResponse> getByChuongTrinh(Long chuongTrinhId) {
        return khungRepository
                .findByChuongTrinhDaoTao_IdOrderByHocKyAscThuTuAsc(chuongTrinhId)
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<KhungChuongTrinhResponse> getByHocKy(Long chuongTrinhId, Integer hocKy) {
        return khungRepository
                .findByChuongTrinhDaoTao_IdAndHocKy(chuongTrinhId, hocKy)
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<KhungChuongTrinhResponse> getByNhomKienThuc(Long chuongTrinhId, String nhomKienThuc) {
        return khungRepository
                .findByChuongTrinhDaoTao_IdAndNhomKienThuc(chuongTrinhId, nhomKienThuc)
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getTongTinChi(Long chuongTrinhId) {
        Integer tong = khungRepository.sumTinChiByChuongTrinh(chuongTrinhId);
        return tong != null ? tong : 0;
    }

    @Override
    @Transactional
    public KhungChuongTrinhResponse create(KhungChuongTrinhRequest request) {
        ChuongTrinhDaoTao ct = chuongTrinhRepository.findById(request.getChuongTrinhId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chương trình ID: " + request.getChuongTrinhId()));
        MonHoc mon = monHocRepository.findById(request.getMonHocId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy môn học ID: " + request.getMonHocId()));

        if (khungRepository.existsByChuongTrinhDaoTao_IdAndMonHoc_Id(
                request.getChuongTrinhId(), request.getMonHocId())) {
            throw new BadRequestException("Môn học đã tồn tại trong khung chương trình này");
        }

        KhungChuongTrinh khung = KhungChuongTrinh.builder()
                .chuongTrinhDaoTao(ct)
                .monHoc(mon)
                .hocKy(request.getHocKy())
                .nhomKienThuc(request.getNhomKienThuc())
                .loaiMon(request.getLoaiMon())
                .thuTu(request.getThuTu())
                .ghiChu(request.getGhiChu())
                .build();
        return toResponse(khungRepository.save(khung));
    }

    @Override
    @Transactional
    public KhungChuongTrinhResponse update(Long id, KhungChuongTrinhRequest request) {
        KhungChuongTrinh khung = findById(id);
        MonHoc mon = monHocRepository.findById(request.getMonHocId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy môn học ID: " + request.getMonHocId()));

        khung.setMonHoc(mon);
        khung.setHocKy(request.getHocKy());
        khung.setNhomKienThuc(request.getNhomKienThuc());
        khung.setLoaiMon(request.getLoaiMon());
        khung.setThuTu(request.getThuTu());
        khung.setGhiChu(request.getGhiChu());
        return toResponse(khungRepository.save(khung));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        khungRepository.delete(findById(id));
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private KhungChuongTrinh findById(Long id) {
        return khungRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khung chương trình ID: " + id));
    }

    private KhungChuongTrinhResponse toResponse(KhungChuongTrinh k) {
        MonHoc m = k.getMonHoc();
        ChuongTrinhDaoTao ct = k.getChuongTrinhDaoTao();
        return KhungChuongTrinhResponse.builder()
                .id(k.getId())
                .chuongTrinhId(ct != null ? ct.getId() : null)
                .tenChuongTrinh(ct != null ? ct.getTenChuongTrinh() : null)
                .monHocId(m != null ? m.getId() : null)
                .maMon(m != null ? m.getMaMon() : null)
                .tenMon(m != null ? m.getTenMon() : null)
                .tinChi(m != null ? m.getTinChi() : null)
                .hocKy(k.getHocKy())
                .nhomKienThuc(k.getNhomKienThuc())
                .loaiMon(k.getLoaiMon())
                .thuTu(k.getThuTu())
                .ghiChu(k.getGhiChu())
                .build();
    }
    public KhungChuongTrinhResponse getById(Long id) {
    return toResponse(findById(id));
}
}