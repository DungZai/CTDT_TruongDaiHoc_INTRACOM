/**
 * authApi.js — API calls cho authentication
 */
const authApi = (() => {

  /**
   * Đăng nhập
   * @param {string} username 
   * @param {string} password 
   * @returns {Promise<object>} User data + token
   */
  async function login(username, password) {
    try {
      // ✅ Gọi API login (không cần token)
      const response = await fetch(ENV.BASE_URL + '/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        credentials: 'include', // ✅ QUAN TRỌNG
        body: JSON.stringify({ username, password })
      });

      const result = await response.json();

      if (!response.ok) {
        throw new Error(result.message || 'Đăng nhập thất bại');
      }

      // ✅ result = { success: true, message: "...", data: { token, username, ... } }
      if (result.success && result.data?.token) {
        // ✅ Lưu token và thông tin user
        TokenService.save(result.data.token);
        
        // ✅ Lưu thêm thông tin user (optional)
        if (typeof localStorage !== 'undefined') {
          localStorage.setItem('user', JSON.stringify({
            id: result.data.id,
            username: result.data.username,
            email: result.data.email,
            role: result.data.role
          }));
        }

        return result.data;
      } else {
        throw new Error(result.message || 'Đăng nhập thất bại');
      }
      
    } catch (error) {
      console.error('Login error:', error);
      throw error;
    }
  }

  /**
   * Đăng xuất
   */
  function logout() {
    TokenService.clear();
    if (typeof localStorage !== 'undefined') {
      localStorage.removeItem('user');
    }
    window.location.href = '/pages/login.html';
  }

  /**
   * Kiểm tra đã đăng nhập chưa
   */
  function isAuthenticated() {
    return !!TokenService.get();
  }

  /**
   * Lấy thông tin user hiện tại
   */
  function getCurrentUser() {
    try {
      const userStr = localStorage.getItem('user');
      return userStr ? JSON.parse(userStr) : null;
    } catch {
      return null;
    }
  }

  /**
   * Đổi mật khẩu
   */
  async function changePassword(currentPassword, newPassword) {
    return http.put('/api/auth/change-password', {
      currentPassword,
      newPassword
    });
  }

  return {
    login,
    logout,
    isAuthenticated,
    getCurrentUser,
    changePassword
  };
})();