package vn.intracom.chuongtrinhdaotao.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.intracom.chuongtrinhdaotao.dto.request.NganhRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.NganhResponse;
import vn.intracom.chuongtrinhdaotao.entity.Nganh;
import vn.intracom.chuongtrinhdaotao.exception.BadRequestException;
import vn.intracom.chuongtrinhdaotao.exception.ResourceNotFoundException;
import vn.intracom.chuongtrinhdaotao.repository.NganhRepository;
import vn.intracom.chuongtrinhdaotao.service.INganhService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NganhServiceImpl implements INganhService {

    private final NganhRepository nganhRepository;

    @Override
    @Transactional(readOnly = true)
    public List<NganhResponse> getAll() {
        return nganhRepository.findAll()
                .stream().map(this::toResponse).toList();
    }


    @Override
@Transactional(readOnly = true)
public List<NganhResponse> getAllForSelect() {
    return nganhRepository.findByTrangThai(true)
            .stream().map(this::toResponse).toList();
}

    @Override
    @Transactional(readOnly = true)
    public NganhResponse getById(Long id) {
        return toResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<NganhResponse> getByTrangThai(Boolean trangThai) {
        return nganhRepository.findByTrangThai(trangThai)
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public NganhResponse create(NganhRequest request) {
        if (nganhRepository.existsByMaNganh(request.getMaNganh())) {
            throw new BadRequestException("Mã ngành '" + request.getMaNganh() + "' đã tồn tại");
        }
        Nganh nganh = Nganh.builder()
                .maNganh(request.getMaNganh())
                .tenNganh(request.getTenNganh())
                .moTa(request.getMoTa())
                .trangThai(request.getTrangThai())
                .build();
        return toResponse(nganhRepository.save(nganh));
    }

    @Override
    @Transactional
    public NganhResponse update(Long id, NganhRequest request) {
        Nganh nganh = findById(id);
        // Cho phép giữ mã ngành cũ, chỉ báo lỗi nếu đổi sang mã đã có
        if (!nganh.getMaNganh().equals(request.getMaNganh())
                && nganhRepository.existsByMaNganh(request.getMaNganh())) {
            throw new BadRequestException("Mã ngành '" + request.getMaNganh() + "' đã tồn tại");
        }
        nganh.setMaNganh(request.getMaNganh());
        nganh.setTenNganh(request.getTenNganh());
        nganh.setMoTa(request.getMoTa());
        nganh.setTrangThai(request.getTrangThai());
        return toResponse(nganhRepository.save(nganh));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        nganhRepository.delete(findById(id));
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private Nganh findById(Long id) {
        return nganhRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ngành ID: " + id));
    }

    private NganhResponse toResponse(Nganh n) {
        return NganhResponse.builder()
                .id(n.getId())
                .maNganh(n.getMaNganh())
                .tenNganh(n.getTenNganh())
                .moTa(n.getMoTa())
                .trangThai(n.getTrangThai())
                .build();
    }
}