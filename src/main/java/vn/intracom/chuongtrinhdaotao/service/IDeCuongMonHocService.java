package vn.intracom.chuongtrinhdaotao.service;

import vn.intracom.chuongtrinhdaotao.dto.request.DeCuongMonHocRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.DeCuongMonHocResponse;

public interface IDeCuongMonHocService {
    DeCuongMonHocResponse getById(Long id);
    DeCuongMonHocResponse getByMonHoc(Long monHocId);
    DeCuongMonHocResponse create(DeCuongMonHocRequest request);
    DeCuongMonHocResponse update(Long id, DeCuongMonHocRequest request);
    void delete(Long id);
}