const khungApi = {
  getByChuongTrinh: ctId           => http.get(`/api/khung-chuong-trinh/by-chuong-trinh/${ctId}`),
  getByHocKy:       (ctId, hocKy)  => http.get(`/api/khung-chuong-trinh/by-chuong-trinh/${ctId}/hoc-ky/${hocKy}`),
  getByNhomKienThuc:(ctId, nhom)   => http.get(`/api/khung-chuong-trinh/by-chuong-trinh/${ctId}/nhom-kien-thuc?nhomKienThuc=${encodeURIComponent(nhom)}`),
  getTongTinChi:    ctId           => http.get(`/api/khung-chuong-trinh/tong-tin-chi/${ctId}`),
  create:           body           => http.post('/api/khung-chuong-trinh', body),
  update:           (id, b)        => http.put(`/api/khung-chuong-trinh/${id}`, b),
  delete:           id             => http.delete(`/api/khung-chuong-trinh/${id}`),
};