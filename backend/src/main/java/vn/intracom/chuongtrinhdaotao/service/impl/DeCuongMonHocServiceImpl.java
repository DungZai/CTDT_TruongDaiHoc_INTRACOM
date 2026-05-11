package vn.intracom.chuongtrinhdaotao.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.intracom.chuongtrinhdaotao.dto.request.DeCuongMonHocRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.DeCuongMonHocResponse;
import vn.intracom.chuongtrinhdaotao.entity.DeCuongMonHoc;
import vn.intracom.chuongtrinhdaotao.entity.MonHoc;
import vn.intracom.chuongtrinhdaotao.exception.BadRequestException;
import vn.intracom.chuongtrinhdaotao.exception.ResourceNotFoundException;
import vn.intracom.chuongtrinhdaotao.repository.DeCuongMonHocRepository;
import vn.intracom.chuongtrinhdaotao.repository.MonHocRepository;
import vn.intracom.chuongtrinhdaotao.service.IDeCuongMonHocService;

@Service
@RequiredArgsConstructor
public class DeCuongMonHocServiceImpl implements IDeCuongMonHocService {

    private final DeCuongMonHocRepository deCuongRepository;
    private final MonHocRepository monHocRepository;

    @Override
    @Transactional(readOnly = true)
    public DeCuongMonHocResponse getById(Long id) {
        return toResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public DeCuongMonHocResponse getByMonHoc(Long monHocId) {
        return deCuongRepository.findByMonHoc_Id(monHocId)
                .map(this::toResponse)
                .orElse(null);
    }

    @Override
    @Transactional
    public DeCuongMonHocResponse create(DeCuongMonHocRequest request) {
        MonHoc mon = monHocRepository.findById(request.getMonHocId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy môn học ID: " + request.getMonHocId()));

        if (deCuongRepository.existsByMonHoc_Id(request.getMonHocId())) {
            throw new BadRequestException("Môn học này đã có đề cương, vui lòng dùng chức năng cập nhật");
        }

        DeCuongMonHoc dc = DeCuongMonHoc.builder()
                .monHoc(mon)
                .mucTieu(request.getMucTieu())
                .noiDung(request.getNoiDung())
                .phuongPhapDay(request.getPhuongPhapDay())
                .phuongPhapDanhGia(request.getPhuongPhapDanhGia())
                .taiLieu(request.getTaiLieu())
                .version(request.getVersion())
                .build();
        return toResponse(deCuongRepository.save(dc));
    }

    @Override
    @Transactional
    public DeCuongMonHocResponse update(Long id, DeCuongMonHocRequest request) {
        DeCuongMonHoc dc = findById(id);
        dc.setMucTieu(request.getMucTieu());
        dc.setNoiDung(request.getNoiDung());
        dc.setPhuongPhapDay(request.getPhuongPhapDay());
        dc.setPhuongPhapDanhGia(request.getPhuongPhapDanhGia());
        dc.setTaiLieu(request.getTaiLieu());
        dc.setVersion(request.getVersion());
        return toResponse(deCuongRepository.save(dc));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        deCuongRepository.delete(findById(id));
    }

    // ── helpers

    private DeCuongMonHoc findById(Long id) {
        return deCuongRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đề cương ID: " + id));
    }

    private DeCuongMonHocResponse toResponse(DeCuongMonHoc dc) {
        MonHoc m = dc.getMonHoc();
        return DeCuongMonHocResponse.builder()
                .id(dc.getId())
                .monHocId(m != null ? m.getId() : null)
                .maMon(m != null ? m.getMaMon() : null)
                .tenMon(m != null ? m.getTenMon() : null)
                .mucTieu(dc.getMucTieu())
                .noiDung(dc.getNoiDung())
                .phuongPhapDay(dc.getPhuongPhapDay())
                .phuongPhapDanhGia(dc.getPhuongPhapDanhGia())
                .taiLieu(dc.getTaiLieu())
                .version(dc.getVersion())
                .build();
    }
}