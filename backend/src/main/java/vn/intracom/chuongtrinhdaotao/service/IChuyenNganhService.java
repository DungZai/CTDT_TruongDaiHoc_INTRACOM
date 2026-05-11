package vn.intracom.chuongtrinhdaotao.service;
import vn.intracom.chuongtrinhdaotao.dto.request.ChuyenNganhRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.ChuyenNganhResponse;
import vn.intracom.chuongtrinhdaotao.dto.response.PageResponse;

import java.util.List;

public interface IChuyenNganhService {
    List<ChuyenNganhResponse> getAllForSelect();
    PageResponse<ChuyenNganhResponse> getAll(String keyword, Long nganhId, int page, int size);
    ChuyenNganhResponse getById(Long id);
    List<ChuyenNganhResponse> getByNganh(Long nganhId);
    ChuyenNganhResponse create(ChuyenNganhRequest request);
    ChuyenNganhResponse update(Long id, ChuyenNganhRequest request);
    void delete(Long id);
}