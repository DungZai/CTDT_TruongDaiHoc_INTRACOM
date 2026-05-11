package vn.intracom.chuongtrinhdaotao.service;

import vn.intracom.chuongtrinhdaotao.dto.request.ChuanDauRaRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.ChuanDauRaResponse;

import java.util.List;

public interface IChuanDauRaService {
    List<ChuanDauRaResponse> getByChuongTrinh(Long chuongTrinhId);
    ChuanDauRaResponse getById(Long id);
    ChuanDauRaResponse create(ChuanDauRaRequest request);
    ChuanDauRaResponse update(Long id, ChuanDauRaRequest request);
    void delete(Long id);
}