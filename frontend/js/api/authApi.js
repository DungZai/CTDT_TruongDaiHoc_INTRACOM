const authApi = {
  async login(username, password) {
    const res = await http.post('/api/auth/login', { username, password });
    console.log('Login response:', res); // Debug — xem backend trả về gì
    // res đã được unwrap bởi client.js
    // Nếu backend trả ApiResponse { success, data: { token } }
    // thì client.js đã unwrap → res = { token, type, id, username, email, role }
    if (res?.token) {
      TokenService.save(res.token);
      return res;
    }
    // Nếu chưa unwrap đúng
    if (res?.data?.token) {
      TokenService.save(res.data.token);
      return res.data;
    }
    throw new Error('Không nhận được token từ server');
  },

  logout() {
    TokenService.clear();
    window.location.href = '/frontend/pages/login.html';
  },
};