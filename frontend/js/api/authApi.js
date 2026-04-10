const authApi = {
  async login(username, password) {
    const res = await http.post('/api/auth/login', { username, password });
    if (res?.token) {
      TokenService.save(res.token);
      localStorage.setItem('userInfo', JSON.stringify({
        username : res.username,
        email    : res.email,
        roleName : res.role,
      }));
      return res;
    }
    throw new Error('Không nhận được token từ server');
  },

  async changePassword({ currentPassword, newPassword }) {
    return http.put('/api/auth/change-password', { currentPassword, newPassword });
  },

  logout() {
    TokenService.clear();
    localStorage.removeItem('userInfo');
    window.location.href = '../pages/login.html';
  },
};