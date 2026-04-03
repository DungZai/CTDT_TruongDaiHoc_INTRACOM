package vn.intracom.chuongtrinhdaotao.service;

import vn.intracom.chuongtrinhdaotao.dto.request.ChuongTrinhDaoTaoRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.ChuongTrinhDaoTaoResponse;

import java.util.List;

public interface IChuongTrinhDaoTaoService {
    List<ChuongTrinhDaoTaoResponse> getAll();
    ChuongTrinhDaoTaoResponse getById(Long id);
    List<ChuongTrinhDaoTaoResponse> getByNganh(Long nganhId);
    List<ChuongTrinhDaoTaoResponse> getByHe(Long heId);
    List<ChuongTrinhDaoTaoResponse> search(String keyword);
    ChuongTrinhDaoTaoResponse create(ChuongTrinhDaoTaoRequest request);
    ChuongTrinhDaoTaoResponse update(Long id, ChuongTrinhDaoTaoRequest request);
    void delete(Long id);
}