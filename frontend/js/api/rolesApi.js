const rolesApi = {
  getAll:  ()       => http.get('/api/roles'),
  getById: id       => http.get(`/api/roles/${id}`),
  create:  roleName => http.post('/api/roles', { roleName }),
  delete:  id       => http.delete(`/api/roles/${id}`),
};