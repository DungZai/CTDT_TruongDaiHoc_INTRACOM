const monHocApi = {
  getAll:          ()        => http.get('/api/mon-hoc'),
  getActive:       ()        => http.get('/api/mon-hoc/active'),
  getById:         id        => http.get(`/api/mon-hoc/${id}`),
  search:          keyword   => http.get(`/api/mon-hoc/search?keyword=${encodeURIComponent(keyword)}`),
  getByChuongTrinh:ctId      => http.get(`/api/mon-hoc/by-chuong-trinh/${ctId}`),
  create:          body      => http.post('/api/mon-hoc', body),
  update:          (id, b)   => http.put(`/api/mon-hoc/${id}`, b),
  delete:          id        => http.delete(`/api/mon-hoc/${id}`),
};