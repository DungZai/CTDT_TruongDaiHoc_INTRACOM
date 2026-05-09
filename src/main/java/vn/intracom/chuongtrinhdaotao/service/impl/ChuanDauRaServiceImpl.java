package vn.intracom.chuongtrinhdaotao.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.intracom.chuongtrinhdaotao.dto.request.ChuanDauRaRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.ChuanDauRaResponse;
import vn.intracom.chuongtrinhdaotao.entity.ChuanDauRa;
import vn.intracom.chuongtrinhdaotao.entity.ChuongTrinhDaoTao;
import vn.intracom.chuongtrinhdaotao.exception.BadRequestException;
import vn.intracom.chuongtrinhdaotao.exception.ResourceNotFoundException;
import vn.intracom.chuongtrinhdaotao.repository.ChuanDauRaRepository;
import vn.intracom.chuongtrinhdaotao.repository.ChuongTrinhDaoTaoRepository;
import vn.intracom.chuongtrinhdaotao.service.IChuanDauRaService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChuanDauRaServiceImpl implements IChuanDauRaService {

    private final ChuanDauRaRepository chuanDauRaRepository;
    private final ChuongTrinhDaoTaoRepository chuongTrinhRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ChuanDauRaResponse> getByChuongTrinh(Long chuongTrinhId) {
        return chuanDauRaRepository.findByChuongTrinhDaoTao_Id(chuongTrinhId)
                .stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ChuanDauRaResponse getById(Long id) {
        return toResponse(findById(id));
    }

    @Override
    @Transactional
    public ChuanDauRaResponse create(ChuanDauRaRequest request) {
        ChuongTrinhDaoTao ct = chuongTrinhRepository.findById(request.getChuongTrinhId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chương trình ID: " + request.getChuongTrinhId()));

        if (chuanDauRaRepository.existsByChuongTrinhDaoTao_IdAndMaChuan(
                request.getChuongTrinhId(), request.getMaChuan())) {
            throw new BadRequestException("Mã chuẩn '" + request.getMaChuan() + "' đã tồn tại trong chương trình này");
        }

        ChuanDauRa cdr = ChuanDauRa.builder()
                .chuongTrinhDaoTao(ct)
                .maChuan(request.getMaChuan())
                .noiDung(request.getNoiDung())
                .build();
        return toResponse(chuanDauRaRepository.save(cdr));
    }

    @Override
    @Transactional
    public ChuanDauRaResponse update(Long id, ChuanDauRaRequest request) {
        ChuanDauRa cdr = findById(id);
        if (!cdr.getMaChuan().equals(request.getMaChuan())
                && chuanDauRaRepository.existsByChuongTrinhDaoTao_IdAndMaChuan(
                        request.getChuongTrinhId(), request.getMaChuan())) {
            throw new BadRequestException("Mã chuẩn '" + request.getMaChuan() + "' đã tồn tại trong chương trình này");
        }
        cdr.setMaChuan(request.getMaChuan());
        cdr.setNoiDung(request.getNoiDung());
        return toResponse(chuanDauRaRepository.save(cdr));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        chuanDauRaRepository.delete(findById(id));
    }


    private ChuanDauRa findById(Long id) {
        return chuanDauRaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chuẩn đầu ra ID: " + id));
    }

    private ChuanDauRaResponse toResponse(ChuanDauRa c) {
        ChuongTrinhDaoTao ct = c.getChuongTrinhDaoTao();
        return ChuanDauRaResponse.builder()
                .id(c.getId())
                .chuongTrinhId(ct != null ? ct.getId() : null)
                .tenChuongTrinh(ct != null ? ct.getTenChuongTrinh() : null)
                .maChuan(c.getMaChuan())
                .noiDung(c.getNoiDung())
                .build();
    }
}