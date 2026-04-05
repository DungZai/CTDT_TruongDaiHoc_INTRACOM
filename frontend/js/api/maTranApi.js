const maTranApi = {
  getByMonHoc:     monId    => http.get(`/api/mon-hoc-chuan-dau-ra/mon-hoc/${monId}`),
  getByChuanDauRa: cdrId    => http.get(`/api/mon-hoc-chuan-dau-ra/chuan-dau-ra/${cdrId}`),
  create:          body     => http.post('/api/mon-hoc-chuan-dau-ra', body),
  update:          (id, b)  => http.put(`/api/mon-hoc-chuan-dau-ra/${id}`, b),
  delete:          id       => http.delete(`/api/mon-hoc-chuan-dau-ra/${id}`),
};