package vn.intracom.chuongtrinhdaotao.service;

import vn.intracom.chuongtrinhdaotao.dto.request.MonHocRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.MonHocResponse;

import java.util.List;

public interface IMonHocService {
    List<MonHocResponse> getAll();
    MonHocResponse getById(Long id);
    List<MonHocResponse> getByTrangThai(Boolean trangThai);
    List<MonHocResponse> search(String keyword);
    List<MonHocResponse> getByChuongTrinh(Long chuongTrinhId);
    MonHocResponse create(MonHocRequest request);
    MonHocResponse update(Long id, MonHocRequest request);
    void delete(Long id);
}