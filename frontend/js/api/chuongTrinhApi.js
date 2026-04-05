const chuongTrinhApi = {
  getAll:     ()        => http.get('/api/chuong-trinh'),
  getById:    id        => http.get(`/api/chuong-trinh/${id}`),
  getByNganh: ngId      => http.get(`/api/chuong-trinh/by-nganh/${ngId}`),
  getByHe:    heId      => http.get(`/api/chuong-trinh/by-he/${heId}`),
  search:     keyword   => http.get(`/api/chuong-trinh/search?keyword=${encodeURIComponent(keyword)}`),
  create:     body      => http.post('/api/chuong-trinh', body),
  update:     (id, b)   => http.put(`/api/chuong-trinh/${id}`, b),
  delete:     id        => http.delete(`/api/chuong-trinh/${id}`),
};