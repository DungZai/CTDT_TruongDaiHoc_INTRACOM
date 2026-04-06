const authApi = {
  async login(username, password) {
    const res = await http.post('/api/auth/login', { username, password });
    if (res?.token) {
      TokenService.save(res.token);
      return res;
    }
    throw new Error('Không nhận được token từ server');
  },

  logout() {
    TokenService.clear();
    window.location.href = '../pages/login.html';
  },
};