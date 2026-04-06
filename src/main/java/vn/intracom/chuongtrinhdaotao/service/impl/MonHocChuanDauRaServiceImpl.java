package vn.intracom.chuongtrinhdaotao.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.intracom.chuongtrinhdaotao.dto.request.MonHocChuanDauRaRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.MonHocChuanDauRaResponse;
import vn.intracom.chuongtrinhdaotao.entity.ChuanDauRa;
import vn.intracom.chuongtrinhdaotao.entity.MonHoc;
import vn.intracom.chuongtrinhdaotao.entity.MonHocChuanDauRa;
import vn.intracom.chuongtrinhdaotao.repository.ChuanDauRaRepository;
import vn.intracom.chuongtrinhdaotao.repository.MonHocChuanDauRaRepository;
import vn.intracom.chuongtrinhdaotao.repository.MonHocRepository;
import vn.intracom.chuongtrinhdaotao.service.IMonHocChuanDauRaService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MonHocChuanDauRaServiceImpl implements IMonHocChuanDauRaService {

    private final MonHocChuanDauRaRepository matranRepository;
    private final MonHocRepository           monHocRepository;
    private final ChuanDauRaRepository       chuanDauRaRepository;

    @Override
    public List<MonHocChuanDauRaResponse> getByMonHoc(Long monHocId) {
        return matranRepository.findByMonHoc_Id(monHocId)
                .stream().map(this::toResponse).toList();
    }

    @Override
    public List<MonHocChuanDauRaResponse> getByChuanDauRa(Long chuanDauRaId) {
        return matranRepository.findByChuanDauRa_Id(chuanDauRaId)
                .stream().map(this::toResponse).toList();
    }

    @Override
    public MonHocChuanDauRaResponse create(MonHocChuanDauRaRequest req) {
        MonHoc    monHoc    = monHocRepository.findById(req.getMonHocId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy môn học."));
        ChuanDauRa cdr     = chuanDauRaRepository.findById(req.getChuanDauRaId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chuẩn đầu ra."));

        MonHocChuanDauRa entity = MonHocChuanDauRa.builder()
                .monHoc(monHoc)
                .chuanDauRa(cdr)
                .mucDo(req.getMucDo())
                .build();

        return toResponse(matranRepository.save(entity));
    }

    @Override
    public MonHocChuanDauRaResponse update(Long id, MonHocChuanDauRaRequest req) {
        MonHocChuanDauRa entity = matranRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bản ghi."));
        entity.setMucDo(req.getMucDo());
        return toResponse(matranRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!matranRepository.existsById(id))
            throw new IllegalArgumentException("Không tìm thấy bản ghi.");
        matranRepository.deleteById(id);
    }

    private MonHocChuanDauRaResponse toResponse(MonHocChuanDauRa e) {
        return MonHocChuanDauRaResponse.builder()
                .id(e.getId())
                .monHocId(e.getMonHoc().getId())
                .maMon(e.getMonHoc().getMaMon())
                .tenMon(e.getMonHoc().getTenMon())
                .chuanDauRaId(e.getChuanDauRa().getId())
                .maChuan(e.getChuanDauRa().getMaChuan())
                .noiDung(e.getChuanDauRa().getNoiDung())
                .mucDo(e.getMucDo())
                .build();
    }
}