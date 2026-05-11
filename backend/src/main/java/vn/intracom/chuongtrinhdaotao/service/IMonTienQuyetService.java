package vn.intracom.chuongtrinhdaotao.service;

import vn.intracom.chuongtrinhdaotao.dto.request.MonTienQuyetRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.MonTienQuyetResponse;

import java.util.List;

public interface IMonTienQuyetService {
    List<MonTienQuyetResponse> getByMonHoc(Long monHocId);
    MonTienQuyetResponse create(MonTienQuyetRequest req);
    void delete(Long id);
}