/**
 * client.js — fetch wrapper, tự đính JWT và unwrap response
 */
const ApiClient = (() => {

  async function request(method, url, body = null) {
    const fullUrl = url.startsWith('http') ? url : ENV.BASE_URL + url;

    const headers = { 'Content-Type': 'application/json' };
    const token = TokenService.get?.();
    
    // ✅ Không gửi token cho auth endpoints
    if (token && !url.includes('/api/auth/')) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    const opts = { method, headers };
    if (body) opts.body = JSON.stringify(body);

    const res = await fetch(fullUrl, opts);

    // ✅ Chỉ redirect khi không phải auth endpoint
    if (res.status === 401) {
      if (!url.includes('/api/auth/')) {
        TokenService.clear?.();
        window.location.href = '../pages/login.html';
        return;
      }
    }

    // Một số API trả về 204 No Content (body rỗng) — đặc biệt là DELETE
    const text = await res.text();
    const json = text ? JSON.parse(text) : null;

    if (!res.ok) {
      throw new Error(json?.message || `Lỗi ${res.status}`);
    }

    // Tự unwrap: { success, message, data } → trả về data
    // Nếu không có wrapper hoặc body rỗng thì trả nguyên json
    return json?.data !== undefined ? json.data : json;
  }

  return {
    get:    (url)        => request('GET',    url),
    post:   (url, body)  => request('POST',   url, body),
    put:    (url, body)  => request('PUT',    url, body),
    delete: (url)        => request('DELETE', url),
  };
})();

// Alias để tương thích với các file *Api.js đang dùng `http`
const http = ApiClient;