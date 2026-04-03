package vn.intracom.chuongtrinhdaotao.service;
import vn.intracom.chuongtrinhdaotao.dto.request.NganhRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.NganhResponse;

import java.util.List;

public interface INganhService {
    List<NganhResponse> getAll();
    NganhResponse getById(Long id);
    List<NganhResponse> getByTrangThai(Boolean trangThai);
    NganhResponse create(NganhRequest request);
    NganhResponse update(Long id, NganhRequest request);
    void delete(Long id);
}