package vn.intracom.chuongtrinhdaotao.service;

import vn.intracom.chuongtrinhdaotao.dto.request.MonHocChuanDauRaRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.MonHocChuanDauRaResponse;

import java.util.List;

public interface IMonHocChuanDauRaService {
    List<MonHocChuanDauRaResponse> getByMonHoc(Long monHocId);
    List<MonHocChuanDauRaResponse> getByChuanDauRa(Long chuanDauRaId);
    MonHocChuanDauRaResponse create(MonHocChuanDauRaRequest req);
    MonHocChuanDauRaResponse update(Long id, MonHocChuanDauRaRequest req);
    void delete(Long id);
}