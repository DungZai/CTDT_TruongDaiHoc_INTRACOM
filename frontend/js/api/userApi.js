const userApi = {
  getMe:    ()        => http.get('/api/users/me'),
  getAll:   (params = '') => http.get(`/api/users${params ? '?' + params : ''}`), // ← sửa
  getById:  id        => http.get(`/api/users/${id}`),
  create:   body      => http.post('/api/users', body),
  update:   (id, b)   => http.put(`/api/users/${id}`, b),
  delete:   id        => http.delete(`/api/users/${id}`),
  getRoles: ()        => http.get('/api/roles'),
  resetPassword: (id, data) => http.patch(`/api/users/${id}/reset-password`, data),
};