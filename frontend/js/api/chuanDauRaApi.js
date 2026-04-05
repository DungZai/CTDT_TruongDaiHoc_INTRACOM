const chuanDauRaApi = {
  getByChuongTrinh: ctId    => http.get(`/api/chuan-dau-ra/by-chuong-trinh/${ctId}`),
  getById:          id      => http.get(`/api/chuan-dau-ra/${id}`),
  create:           body    => http.post('/api/chuan-dau-ra', body),
  update:           (id, b) => http.put(`/api/chuan-dau-ra/${id}`, b),
  delete:           id      => http.delete(`/api/chuan-dau-ra/${id}`),
};