package vn.intracom.chuongtrinhdaotao.service;

import vn.intracom.chuongtrinhdaotao.dto.request.KhungChuongTrinhRequest;
import vn.intracom.chuongtrinhdaotao.dto.response.KhungChuongTrinhResponse;

import java.util.List;

public interface IKhungChuongTrinhService {
    List<KhungChuongTrinhResponse> getByChuongTrinh(Long chuongTrinhId);
    List<KhungChuongTrinhResponse> getByHocKy(Long chuongTrinhId, Integer hocKy);
    List<KhungChuongTrinhResponse> getByNhomKienThuc(Long chuongTrinhId, String nhomKienThuc);
    Integer getTongTinChi(Long chuongTrinhId);
    KhungChuongTrinhResponse getById(Long id);  // 👈 thêm dòng này
    KhungChuongTrinhResponse create(KhungChuongTrinhRequest request);
    KhungChuongTrinhResponse update(Long id, KhungChuongTrinhRequest request);
    void delete(Long id);
}