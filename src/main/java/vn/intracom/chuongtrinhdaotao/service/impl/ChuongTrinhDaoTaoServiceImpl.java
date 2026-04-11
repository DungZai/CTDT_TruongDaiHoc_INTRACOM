package vn.intracom.chuongtrinhdaotao.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.intracom.chuongtrinhdaotao.dto.request.ChuongTrinhDaoTaoRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.ChuongTrinhDaoTaoResponse;
import vn.intracom.chuongtrinhdaotao.entity.ChuongTrinhDaoTao;
import vn.intracom.chuongtrinhdaotao.entity.HeDaoTao;
import vn.intracom.chuongtrinhdaotao.entity.Nganh;
import vn.intracom.chuongtrinhdaotao.exception.ResourceNotFoundException;
import vn.intracom.chuongtrinhdaotao.repository.ChuongTrinhDaoTaoRepository;
import vn.intracom.chuongtrinhdaotao.repository.HeDaoTaoRepository;
import vn.intracom.chuongtrinhdaotao.repository.NganhRepository;
import vn.intracom.chuongtrinhdaotao.service.IChuongTrinhDaoTaoService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChuongTrinhDaoTaoServiceImpl implements IChuongTrinhDaoTaoService {

    private final ChuongTrinhDaoTaoRepository chuongTrinhRepository;
    private final NganhRepository nganhRepository;
    private final HeDaoTaoRepository heDaoTaoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ChuongTrinhDaoTaoResponse> getAll() {
        return chuongTrinhRepository.findAll()
                .stream().map(this::toResponse).toList();
    }

    @Override
@Transactional(readOnly = true)
public List<ChuongTrinhDaoTaoResponse> getAllForSelect() {
    return chuongTrinhRepository.findByTrangThai(true)
            .stream().map(this::toResponse).toList();
}

    @Override
    @Transactional(readOnly = true)
    public ChuongTrinhDaoTaoResponse getById(Long id) {
        return toResponse(findById(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChuongTrinhDaoTaoResponse> getByNganh(Long nganhId) {
        return chuongTrinhRepository.findByNganh_Id(nganhId)
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChuongTrinhDaoTaoResponse> getByHe(Long heId) {
        return chuongTrinhRepository.findByHeDaoTao_Id(heId)
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChuongTrinhDaoTaoResponse> search(String keyword) {
        return chuongTrinhRepository.searchByTen(keyword)
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public ChuongTrinhDaoTaoResponse create(ChuongTrinhDaoTaoRequest request) {
        Nganh nganh = nganhRepository.findById(request.getNganhId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ngành ID: " + request.getNganhId()));
        HeDaoTao he = heDaoTaoRepository.findById(request.getHeId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hệ đào tạo ID: " + request.getHeId()));

                if (chuongTrinhRepository.existsByTenChuongTrinhAndNganh_IdAndHeDaoTao_IdAndNamPhatHanh(
        request.getTenChuongTrinh(), request.getNganhId(),
        request.getHeId(), request.getNamPhatHanh())) {
        throw new ResourceNotFoundException("Chương trình đào tạo đã tồn tại!");
}
        ChuongTrinhDaoTao ct = ChuongTrinhDaoTao.builder()
                .tenChuongTrinh(request.getTenChuongTrinh())
                .nganh(nganh)
                .heDaoTao(he)
                .tongTinChi(request.getTongTinChi())
                .namPhatHanh(request.getNamPhatHanh())
                .moTa(request.getMoTa())
                .trangThai(request.getTrangThai())
                .build();
        return toResponse(chuongTrinhRepository.save(ct));
    }

    @Override
    @Transactional
    public ChuongTrinhDaoTaoResponse update(Long id, ChuongTrinhDaoTaoRequest request) {
        ChuongTrinhDaoTao ct = findById(id);
        Nganh nganh = nganhRepository.findById(request.getNganhId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy ngành ID: " + request.getNganhId()));
        HeDaoTao he = heDaoTaoRepository.findById(request.getHeId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hệ đào tạo ID: " + request.getHeId()));

                if (chuongTrinhRepository.existsByTenChuongTrinhAndNganh_IdAndHeDaoTao_IdAndNamPhatHanhAndIdNot(
        request.getTenChuongTrinh(), request.getNganhId(),
        request.getHeId(), request.getNamPhatHanh(), id)) {
        throw new ResourceNotFoundException("Chương trình đào tạo đã tồn tại!");
}

        ct.setTenChuongTrinh(request.getTenChuongTrinh());
        ct.setNganh(nganh);
        ct.setHeDaoTao(he);
        ct.setTongTinChi(request.getTongTinChi());
        ct.setNamPhatHanh(request.getNamPhatHanh());
        ct.setMoTa(request.getMoTa());
        ct.setTrangThai(request.getTrangThai());
        return toResponse(chuongTrinhRepository.save(ct));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        chuongTrinhRepository.delete(findById(id));
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private ChuongTrinhDaoTao findById(Long id) {
        return chuongTrinhRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chương trình ID: " + id));
    }

    private ChuongTrinhDaoTaoResponse toResponse(ChuongTrinhDaoTao ct) {
        return ChuongTrinhDaoTaoResponse.builder()
                .id(ct.getId())
                .tenChuongTrinh(ct.getTenChuongTrinh())
                .nganhId(ct.getNganh() != null ? ct.getNganh().getId() : null)
                .tenNganh(ct.getNganh() != null ? ct.getNganh().getTenNganh() : null)
                .heId(ct.getHeDaoTao() != null ? ct.getHeDaoTao().getId() : null)
                .tenHe(ct.getHeDaoTao() != null ? ct.getHeDaoTao().getTenHe() : null)
                .tongTinChi(ct.getTongTinChi())
                .namPhatHanh(ct.getNamPhatHanh())
                .moTa(ct.getMoTa())
                .trangThai(ct.getTrangThai())
                .build();
    }
}