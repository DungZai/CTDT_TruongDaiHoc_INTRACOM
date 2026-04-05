async function request(method, path, body = null) {
  const headers = { 'Content-Type': 'application/json' };
  const token   = TokenService.get();
  if (token) headers['Authorization'] = `Bearer ${token}`;

  const ctrl = new AbortController();
  const tid  = setTimeout(() => ctrl.abort(), 10000);
  Loading.show();

  try {
    const opts = { method, headers, signal: ctrl.signal };
    if (body) opts.body = JSON.stringify(body);

    const res = await fetch('http://localhost:8080' + path, opts);
    clearTimeout(tid);

    if (res.status === 401) {
      TokenService.clear();
      window.location.href = '/frontend/pages/login.html';
      return;
    }
    if (res.status === 403) {
      Toast.error('Bạn không có quyền thực hiện thao tác này.');
      return null;
    }
    if (!res.ok) {
      const e = await res.json().catch(() => ({}));
      throw new Error(e.message || `Lỗi ${res.status}`);
    }

    const text = await res.text();
    if (!text) return null;
    const json = JSON.parse(text);

    // Nếu backend dùng ApiResponse wrapper { success, message, data }
    if (json && typeof json.success === 'boolean') {
      if (!json.success) throw new Error(json.message || 'Có lỗi xảy ra');
      // Trả về data nếu có, ngược lại trả về toàn bộ json
      return json.data !== undefined ? json.data : json;
    }

    // Không có wrapper (VD: RolesController) → trả về trực tiếp
    return json;
  } catch (err) {
    clearTimeout(tid);
    if (err.name === 'AbortError') throw new Error('Yêu cầu quá thời gian, vui lòng thử lại.');
    throw err;
  } finally {
    Loading.hide();
  }
}

const http = {
  get:    path        => request('GET',    path),
  post:   (path, b)   => request('POST',   path, b),
  put:    (path, b)   => request('PUT',    path, b),
  patch:  (path, b)   => request('PATCH',  path, b),
  delete: path        => request('DELETE', path),
};