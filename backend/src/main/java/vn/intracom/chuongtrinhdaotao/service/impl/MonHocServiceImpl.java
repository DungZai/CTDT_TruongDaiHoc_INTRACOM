package vn.intracom.chuongtrinhdaotao.service.impl;
 
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.intracom.chuongtrinhdaotao.dto.request.MonHocRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.MonHocResponse;
import vn.intracom.chuongtrinhdaotao.dto.response.PageResponse;
import vn.intracom.chuongtrinhdaotao.entity.MonHoc;
import vn.intracom.chuongtrinhdaotao.exception.BadRequestException;
import vn.intracom.chuongtrinhdaotao.exception.ResourceNotFoundException;
import vn.intracom.chuongtrinhdaotao.repository.MonHocRepository;
import vn.intracom.chuongtrinhdaotao.service.IMonHocService;
 
import java.util.List;
 
@Service
@RequiredArgsConstructor
public class MonHocServiceImpl implements IMonHocService {
 
    private final MonHocRepository monHocRepository;
 
    // ── Phân trang + tìm kiếm
    @Override
    @Transactional(readOnly = true)
    public PageResponse<MonHocResponse> getAll(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("maMon").ascending());
        return PageResponse.of(
                monHocRepository.search(keyword, pageable)
                                .map(this::toResponse)
        );
    }
 
    // ── Dùng cho dropdown (không phân trang) 
    @Override
    @Transactional(readOnly = true)
    public List<MonHocResponse> getAllForSelect() {
        return monHocRepository.findByTrangThai(true)
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
        MonHoc monHoc = MonHoc.builder()
                .maMon(request.getMaMon())
                .tenMon(request.getTenMon())
                .tinChi(request.getTinChi())
                .soTietLt(request.getSoTietLt())
                .soTietTh(request.getSoTietTh())
                .moTa(request.getMoTa())
                .trangThai(request.getTrangThai())
                .build();
        return toResponse(monHocRepository.save(monHoc));
    }
 
    @Override
    @Transactional
    public MonHocResponse update(Long id, MonHocRequest request) {
        MonHoc monHoc = findById(id);
        if (!monHoc.getMaMon().equals(request.getMaMon())
                && monHocRepository.existsByMaMon(request.getMaMon())) {
            throw new BadRequestException("Mã môn '" + request.getMaMon() + "' đã tồn tại");
        }
        monHoc.setMaMon(request.getMaMon());
        monHoc.setTenMon(request.getTenMon());
        monHoc.setTinChi(request.getTinChi());
        monHoc.setSoTietLt(request.getSoTietLt());
        monHoc.setSoTietTh(request.getSoTietTh());
        monHoc.setMoTa(request.getMoTa());
        monHoc.setTrangThai(request.getTrangThai());
        return toResponse(monHocRepository.save(monHoc));
    }
 
    @Override
    @Transactional
    public void delete(Long id) {
        monHocRepository.delete(findById(id));
    }
 
    // ── Helpers 
    private MonHoc findById(Long id) {
        return monHocRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy môn học ID: " + id));
    }
 
    private MonHocResponse toResponse(MonHoc m) {
        return MonHocResponse.builder()
                .id(m.getId())
                .maMon(m.getMaMon())
                .tenMon(m.getTenMon())
                .tinChi(m.getTinChi())
                .soTietLt(m.getSoTietLt())
                .soTietTh(m.getSoTietTh())
                .moTa(m.getMoTa())
                .trangThai(m.getTrangThai())
                .build();
    }
}