package vn.intracom.chuongtrinhdaotao.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.intracom.chuongtrinhdaotao.dto.request.HeDaoTaoRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.HeDaoTaoResponse;
import vn.intracom.chuongtrinhdaotao.entity.HeDaoTao;
import vn.intracom.chuongtrinhdaotao.exception.BadRequestException;
import vn.intracom.chuongtrinhdaotao.exception.ResourceNotFoundException;
import vn.intracom.chuongtrinhdaotao.repository.HeDaoTaoRepository;
import vn.intracom.chuongtrinhdaotao.service.IHeDaoTaoService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HeDaoTaoServiceImpl implements IHeDaoTaoService {

    private final HeDaoTaoRepository heDaoTaoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<HeDaoTaoResponse> getAll() {
        return heDaoTaoRepository.findAll()
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HeDaoTaoResponse getById(Long id) {
        return toResponse(findById(id));
    }

    @Override
    @Transactional
    public HeDaoTaoResponse create(HeDaoTaoRequest request) {
        if (heDaoTaoRepository.existsByTenHe(request.getTenHe())) {
            throw new BadRequestException("Tên hệ '" + request.getTenHe() + "' đã tồn tại");
        }
        HeDaoTao he = HeDaoTao.builder()
                .tenHe(request.getTenHe())
                .thoiGianDaoTao(request.getThoiGianDaoTao())
                .tongTinChiMacDinh(request.getTongTinChiMacDinh())
                .moTa(request.getMoTa())
                .build();
        return toResponse(heDaoTaoRepository.save(he));
    }

    @Override
    @Transactional
    public HeDaoTaoResponse update(Long id, HeDaoTaoRequest request) {
        HeDaoTao he = findById(id);
        if (!he.getTenHe().equals(request.getTenHe())
                && heDaoTaoRepository.existsByTenHe(request.getTenHe())) {
            throw new BadRequestException("Tên hệ '" + request.getTenHe() + "' đã tồn tại");
        }
        he.setTenHe(request.getTenHe());
        he.setThoiGianDaoTao(request.getThoiGianDaoTao());
        he.setTongTinChiMacDinh(request.getTongTinChiMacDinh());
        he.setMoTa(request.getMoTa());
        return toResponse(heDaoTaoRepository.save(he));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        heDaoTaoRepository.delete(findById(id));
    }

    // ── helpers 

    private HeDaoTao findById(Long id) {
        return heDaoTaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy hệ đào tạo ID: " + id));
    }

    private HeDaoTaoResponse toResponse(HeDaoTao h) {
        return HeDaoTaoResponse.builder()
                .id(h.getId())
                .tenHe(h.getTenHe())
                .thoiGianDaoTao(h.getThoiGianDaoTao())
                .tongTinChiMacDinh(h.getTongTinChiMacDinh())
                .moTa(h.getMoTa())
                .build();
    }
}