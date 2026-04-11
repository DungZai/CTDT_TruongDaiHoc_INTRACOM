package vn.intracom.chuongtrinhdaotao.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.intracom.chuongtrinhdaotao.dto.request.ChuyenNganhRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.ChuyenNganhResponse;
import vn.intracom.chuongtrinhdaotao.dto.response.PageResponse;
import vn.intracom.chuongtrinhdaotao.entity.ChuyenNganh;
import vn.intracom.chuongtrinhdaotao.entity.Nganh;
import vn.intracom.chuongtrinhdaotao.exception.ResourceNotFoundException;
import vn.intracom.chuongtrinhdaotao.repository.ChuyenNganhRepository;
import vn.intracom.chuongtrinhdaotao.repository.NganhRepository;
import vn.intracom.chuongtrinhdaotao.service.IChuyenNganhService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChuyenNganhServiceImpl implements IChuyenNganhService {

    private final ChuyenNganhRepository chuyenNganhRepository;
    private final NganhRepository nganhRepository;


    @Override
@Transactional(readOnly = true)
public List<ChuyenNganhResponse> getAllForSelect() {
    return chuyenNganhRepository.findByTrangThai(true)
            .stream().map(this::toResponse).toList();
}

    @Override
@Transactional(readOnly = true)
public PageResponse<ChuyenNganhResponse> getAll(String keyword, Long nganhId, int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by("tenChuyenNganh").ascending());
    return PageResponse.of(
            chuyenNganhRepository.search(keyword, nganhId, pageable)
                                 .map(this::toResponse)
    );
}

    @Override
    @Transactional(readOnly = true)
    public ChuyenNganhResponse getById(Long id) {
        return toResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChuyenNganhResponse> getByNganh(Long nganhId) {
        return chuyenNganhRepository.findByNganh_Id(nganhId)
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public ChuyenNganhResponse create(ChuyenNganhRequest request) {
        Nganh nganh = nganhRepository.findById(request.getNganhId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ngành ID: " + request.getNganhId()));
        ChuyenNganh cn = ChuyenNganh.builder()
                .tenChuyenNganh(request.getTenChuyenNganh())
                .nganh(nganh)
                .moTa(request.getMoTa())
                .trangThai(request.getTrangThai())
                .build();
        return toResponse(chuyenNganhRepository.save(cn));
    }

    @Override
    @Transactional
    public ChuyenNganhResponse update(Long id, ChuyenNganhRequest request) {
        ChuyenNganh cn = findById(id);
        Nganh nganh = nganhRepository.findById(request.getNganhId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ngành ID: " + request.getNganhId()));
        cn.setTenChuyenNganh(request.getTenChuyenNganh());
        cn.setNganh(nganh);
        cn.setMoTa(request.getMoTa());
        cn.setTrangThai(request.getTrangThai());
        return toResponse(chuyenNganhRepository.save(cn));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        chuyenNganhRepository.delete(findById(id));
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private ChuyenNganh findById(Long id) {
        return chuyenNganhRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chuyên ngành ID: " + id));
    }

    private ChuyenNganhResponse toResponse(ChuyenNganh cn) {
        return ChuyenNganhResponse.builder()
                .id(cn.getId())
                .tenChuyenNganh(cn.getTenChuyenNganh())
                .nganhId(cn.getNganh() != null ? cn.getNganh().getId() : null)
                .tenNganh(cn.getNganh() != null ? cn.getNganh().getTenNganh() : null)
                .moTa(cn.getMoTa())
                .trangThai(cn.getTrangThai())
                .build();
    }
}