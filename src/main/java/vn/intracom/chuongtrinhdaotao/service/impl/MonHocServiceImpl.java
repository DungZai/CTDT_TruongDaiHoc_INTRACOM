package vn.intracom.chuongtrinhdaotao.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.intracom.chuongtrinhdaotao.dto.request.MonHocRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.MonHocResponse;
import vn.intracom.chuongtrinhdaotao.dto.response.MonHocSimpleResponse;
import vn.intracom.chuongtrinhdaotao.entity.MonHoc;
import vn.intracom.chuongtrinhdaotao.exception.BadRequestException;
import vn.intracom.chuongtrinhdaotao.exception.ResourceNotFoundException;
import vn.intracom.chuongtrinhdaotao.repository.MonHocRepository;
import vn.intracom.chuongtrinhdaotao.repository.MonTienQuyetRepository;
import vn.intracom.chuongtrinhdaotao.service.IMonHocService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MonHocServiceImpl implements IMonHocService {

    private final MonHocRepository monHocRepository;
    private final MonTienQuyetRepository monTienQuyetRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MonHocResponse> getAll() {
        return monHocRepository.findAll()
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MonHocResponse getById(Long id) {
        return toResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonHocResponse> getByTrangThai(Boolean trangThai) {
        return monHocRepository.findByTrangThai(trangThai)
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonHocResponse> search(String keyword) {
        return monHocRepository.searchByTenMon(keyword)
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MonHocResponse> getByChuongTrinh(Long chuongTrinhId) {
        return monHocRepository.findByChuongTrinh(chuongTrinhId)
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public MonHocResponse create(MonHocRequest request) {
        if (monHocRepository.existsByMaMon(request.getMaMon())) {
            throw new BadRequestException("Mã môn '" + request.getMaMon() + "' đã tồn tại");
        }
        MonHoc mon = MonHoc.builder()
                .maMon(request.getMaMon())
                .tenMon(request.getTenMon())
                .tinChi(request.getTinChi())
                .soTietLt(request.getSoTietLt())
                .soTietTh(request.getSoTietTh())
                .moTa(request.getMoTa())
                .trangThai(request.getTrangThai())
                .build();
        return toResponse(monHocRepository.save(mon));
    }

    @Override
    @Transactional
    public MonHocResponse update(Long id, MonHocRequest request) {
        MonHoc mon = findById(id);
        if (!mon.getMaMon().equals(request.getMaMon())
                && monHocRepository.existsByMaMon(request.getMaMon())) {
            throw new BadRequestException("Mã môn '" + request.getMaMon() + "' đã tồn tại");
        }
        mon.setMaMon(request.getMaMon());
        mon.setTenMon(request.getTenMon());
        mon.setTinChi(request.getTinChi());
        mon.setSoTietLt(request.getSoTietLt());
        mon.setSoTietTh(request.getSoTietTh());
        mon.setMoTa(request.getMoTa());
        mon.setTrangThai(request.getTrangThai());
        return toResponse(monHocRepository.save(mon));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        monHocRepository.delete(findById(id));
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private MonHoc findById(Long id) {
        return monHocRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy môn học ID: " + id));
    }

    private MonHocResponse toResponse(MonHoc m) {
        // Lấy danh sách môn tiên quyết (simple — không lồng sâu)
        List<MonHocSimpleResponse> tienQuyet = monTienQuyetRepository
                .findByMonHoc_Id(m.getId())
                .stream()
                .map(mtq -> MonHocSimpleResponse.builder()
                        .id(mtq.getMonTienQuyet().getId())
                        .maMon(mtq.getMonTienQuyet().getMaMon())
                        .tenMon(mtq.getMonTienQuyet().getTenMon())
                        .tinChi(mtq.getMonTienQuyet().getTinChi())
                        .build())
                .toList();

        return MonHocResponse.builder()
                .id(m.getId())
                .maMon(m.getMaMon())
                .tenMon(m.getTenMon())
                .tinChi(m.getTinChi())
                .soTietLt(m.getSoTietLt())
                .soTietTh(m.getSoTietTh())
                .moTa(m.getMoTa())
                .trangThai(m.getTrangThai())
                .danhSachTienQuyet(tienQuyet)
                .build();
    }
}