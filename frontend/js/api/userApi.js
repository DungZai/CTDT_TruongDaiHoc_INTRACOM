const userApi = {
  getMe:   ()       => http.get('/api/users/me'),
  getAll:  ()       => http.get('/api/users'),
  getById: id       => http.get(`/api/users/${id}`),
  delete:  id       => http.delete(`/api/users/${id}`),
};