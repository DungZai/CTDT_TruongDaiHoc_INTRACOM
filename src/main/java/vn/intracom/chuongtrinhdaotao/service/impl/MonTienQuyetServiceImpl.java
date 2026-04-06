package vn.intracom.chuongtrinhdaotao.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.intracom.chuongtrinhdaotao.dto.request.MonTienQuyetRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.MonTienQuyetResponse;
import vn.intracom.chuongtrinhdaotao.entity.MonHoc;
import vn.intracom.chuongtrinhdaotao.entity.MonTienQuyet;
import vn.intracom.chuongtrinhdaotao.repository.MonHocRepository;
import vn.intracom.chuongtrinhdaotao.repository.MonTienQuyetRepository;
import vn.intracom.chuongtrinhdaotao.service.IMonTienQuyetService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MonTienQuyetServiceImpl implements IMonTienQuyetService {

    private final MonTienQuyetRepository monTienQuyetRepository;
    private final MonHocRepository       monHocRepository;

    @Override
    public List<MonTienQuyetResponse> getByMonHoc(Long monHocId) {
        return monTienQuyetRepository.findByMonHoc_Id(monHocId)
                .stream().map(this::toResponse).toList();
    }

    @Override
    public MonTienQuyetResponse create(MonTienQuyetRequest req) {
        if (req.getMonHocId().equals(req.getMonTienQuyetId()))
            throw new IllegalArgumentException("Môn học và môn tiên quyết không được trùng nhau.");

        if (monTienQuyetRepository.existsByMonHoc_IdAndMonTienQuyet_Id(
                req.getMonHocId(), req.getMonTienQuyetId()))
            throw new IllegalArgumentException("Môn tiên quyết này đã tồn tại.");

        MonHoc monHoc       = monHocRepository.findById(req.getMonHocId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy môn học."));
        MonHoc monTienQuyet = monHocRepository.findById(req.getMonTienQuyetId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy môn tiên quyết."));

        MonTienQuyet entity = MonTienQuyet.builder()
                .monHoc(monHoc)
                .monTienQuyet(monTienQuyet)
                .build();

        return toResponse(monTienQuyetRepository.save(entity));
    }

    @Override
    public void delete(Long id) {
        if (!monTienQuyetRepository.existsById(id))
            throw new IllegalArgumentException("Không tìm thấy bản ghi.");
        monTienQuyetRepository.deleteById(id);
    }

    private MonTienQuyetResponse toResponse(MonTienQuyet e) {
        return MonTienQuyetResponse.builder()
                .id(e.getId())
                .monHocId(e.getMonHoc().getId())
                .maMon(e.getMonHoc().getMaMon())
                .tenMon(e.getMonHoc().getTenMon())
                .monTienQuyetId(e.getMonTienQuyet().getId())
                .maMonTienQuyet(e.getMonTienQuyet().getMaMon())
                .tenMonTienQuyet(e.getMonTienQuyet().getTenMon())
                .build();
    }
}