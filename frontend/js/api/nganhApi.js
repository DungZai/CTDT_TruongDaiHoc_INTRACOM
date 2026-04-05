const nganhApi = {
  getAll:    ()       => http.get('/api/nganh'),
  getActive: ()       => http.get('/api/nganh/active'),
  getById:   id       => http.get(`/api/nganh/${id}`),
  create:    body     => http.post('/api/nganh', body),
  update:    (id, b)  => http.put(`/api/nganh/${id}`, b),
  delete:    id       => http.delete(`/api/nganh/${id}`),
};