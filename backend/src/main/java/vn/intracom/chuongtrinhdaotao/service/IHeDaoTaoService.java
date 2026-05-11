package vn.intracom.chuongtrinhdaotao.service;

import vn.intracom.chuongtrinhdaotao.dto.request.HeDaoTaoRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.HeDaoTaoResponse;

import java.util.List;

public interface IHeDaoTaoService {
    List<HeDaoTaoResponse> getAll();
    HeDaoTaoResponse getById(Long id);
    HeDaoTaoResponse create(HeDaoTaoRequest request);
    HeDaoTaoResponse update(Long id, HeDaoTaoRequest request);
    void delete(Long id);
}