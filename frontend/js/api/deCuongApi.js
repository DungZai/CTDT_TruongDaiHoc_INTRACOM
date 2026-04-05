const deCuongApi = {
  getById:     id      => http.get(`/api/de-cuong/${id}`),
  getByMonHoc: monId   => http.get(`/api/de-cuong/by-mon-hoc/${monId}`),
  create:      body    => http.post('/api/de-cuong', body),
  update:      (id, b) => http.put(`/api/de-cuong/${id}`, b),
  delete:      id      => http.delete(`/api/de-cuong/${id}`),
};