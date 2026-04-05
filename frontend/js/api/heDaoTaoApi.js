const heDaoTaoApi = {
  getAll:  ()       => http.get('/api/he-dao-tao'),
  getById: id       => http.get(`/api/he-dao-tao/${id}`),
  create:  body     => http.post('/api/he-dao-tao', body),
  update:  (id, b)  => http.put(`/api/he-dao-tao/${id}`, b),
  delete:  id       => http.delete(`/api/he-dao-tao/${id}`),
};