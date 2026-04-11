const monHocApi = {
  // Phân trang + tìm kiếm: page, size, keyword
  getAll: (params = '') => http.get(`/api/mon-hoc${params ? '?' + params : ''}`),

  // Dùng cho dropdown (không phân trang)
  getAllForSelect: () => http.get('/api/mon-hoc/select'),

  getActive:        ()        => http.get('/api/mon-hoc/active'),
  getById:          id        => http.get(`/api/mon-hoc/${id}`),
  search:           keyword   => http.get(`/api/mon-hoc/search?keyword=${encodeURIComponent(keyword)}`),
  getByChuongTrinh: ctId      => http.get(`/api/mon-hoc/by-chuong-trinh/${ctId}`),
  create:           body      => http.post('/api/mon-hoc', body),
  update:           (id, b)   => http.put(`/api/mon-hoc/${id}`, b),
  delete:           id        => http.delete(`/api/mon-hoc/${id}`),

  // Môn tiên quyết
  getTienQuyet:     monId     => http.get(`/api/mon-tien-quyet/by-mon/${monId}`),
  addTienQuyet:     body      => http.post('/api/mon-tien-quyet', body),
  deleteTienQuyet:  id        => http.delete(`/api/mon-tien-quyet/${id}`),
};