/**
 * client.js — fetch wrapper, tự đính JWT và unwrap response
 */
const ApiClient = (() => {

  async function request(method, url, body = null) {
    const fullUrl = url.startsWith('http') ? url : ENV.BASE_URL + url;

    const headers = { 'Content-Type': 'application/json' };
    const token = TokenService.get();
    
    // ✅ ĐÚNG - Chỉ bỏ qua token cho login/register, VẪN GỬI cho change-password
    const publicEndpoints = ['/api/auth/login', '/api/auth/register'];
    const isPublic = publicEndpoints.some(endpoint => url.includes(endpoint));
    
    if (token && !isPublic) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    const opts = { 
      method, 
      headers,
      credentials: 'include'
    };
    
    if (body) opts.body = JSON.stringify(body);

    try {
      const res = await fetch(fullUrl, opts);

      // ✅ Xử lý 401 Unauthorized
      if (res.status === 401) {
        console.warn('Token không hợp lệ hoặc hết hạn');
        TokenService.clear?.();
        
        // Chỉ redirect nếu không phải trang login
        if (!window.location.pathname.includes('login.html')) {
          window.location.href = '/frontend/pages/login.html';
        }
        throw new Error('Unauthorized');
      }

      // ✅ Xử lý response rỗng (204 No Content)
      const text = await res.text();
      const json = text ? JSON.parse(text) : null;

      // ✅ Xử lý lỗi
      if (!res.ok) {
        const errorMsg = json?.message || json?.error || `Lỗi ${res.status}`;
        throw new Error(errorMsg);
      }

      // ✅ Unwrap response: { success, message, data } → data
      return json?.data !== undefined ? json.data : json;
      
    } catch (error) {
      console.error(`API Error [${method} ${url}]:`, error);
      throw error;
    }
  }

  return {
    get:    (url)        => request('GET',    url),
    post:   (url, body)  => request('POST',   url, body),
    put:    (url, body)  => request('PUT',    url, body),
    delete: (url)        => request('DELETE', url),
    patch:  (url, body)  => request('PATCH',  url, body),
  };
})();

const http = ApiClient;