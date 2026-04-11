const chuyenNganhApi = {
  getAll:         (params = '') => http.get(`/api/chuyen-nganh${params ? '?' + params : ''}`),
  getAllForSelect: ()            => http.get('/api/chuyen-nganh/select'),
  getByNganh: ngId     => http.get(`/api/chuyen-nganh/by-nganh/${ngId}`),
  getById:    id       => http.get(`/api/chuyen-nganh/${id}`),
  create:     body     => http.post('/api/chuyen-nganh', body),
  update:     (id, b)  => http.put(`/api/chuyen-nganh/${id}`, b),
  delete:     id       => http.delete(`/api/chuyen-nganh/${id}`),
};