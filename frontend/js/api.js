const API_BASE = 'http://localhost:8080';

// ── Gọi API chung ─────────────────────────────────────────────────────────────
async function apiCall(method, path, body = null, auth = true) {
  const headers = { 'Content-Type': 'application/json' };
  if (auth) {
    const token = localStorage.getItem('token');
    if (token) headers['Authorization'] = 'Bearer ' + token;
  }
  const opts = { method, headers };
  if (body) opts.body = JSON.stringify(body);

  const res = await fetch(API_BASE + path, opts);

  // Token hết hạn → về trang login
  if (res.status === 401) {
    localStorage.clear();
    window.location.href = '/index.html';
    return;
  }

  const json = await res.json();
  if (!res.ok) throw new Error(json.message || 'Lỗi ' + res.status);

  // API trả về { success, message, data } → lấy data
  // Nếu không có data thì trả về toàn bộ json
  return json.data !== undefined ? json.data : json;
}

// ── Shorthand ─────────────────────────────────────────────────────────────────
const api = {
  get:    (path)         => apiCall('GET',    path),
  post:   (path, body)   => apiCall('POST',   path, body),
  put:    (path, body)   => apiCall('PUT',    path, body),
  delete: (path)         => apiCall('DELETE', path),
  // Không cần auth (login, register)
  postPublic: (path, body) => apiCall('POST', path, body, false),
};

// ── Toast thông báo ───────────────────────────────────────────────────────────
function showToast(msg, type = 'success') {
  let toast = document.getElementById('toast');
  if (!toast) {
    toast = document.createElement('div');
    toast.id = 'toast';
    toast.className = 'toast';
    document.body.appendChild(toast);
  }
  toast.textContent = (type === 'success' ? '✓ ' : '✗ ') + msg;
  toast.className = `toast ${type} show`;
  setTimeout(() => toast.classList.remove('show'), 2800);
}

// ── Modal ─────────────────────────────────────────────────────────────────────
function openModal(title, bodyHtml, onSave) {
  document.getElementById('modal-title').textContent = title;
  document.getElementById('modal-body').innerHTML = bodyHtml;
  document.getElementById('btn-modal-save').onclick = onSave;
  document.getElementById('modal-overlay').classList.add('open');
}
function closeModal() {
  document.getElementById('modal-overlay').classList.remove('open');
}

// ── Toggle switch ─────────────────────────────────────────────────────────────
function makeToggle(id, val) {
  return `<div class="toggle-wrap">
    <div class="toggle ${val ? 'on' : ''}" id="${id}"
         onclick="this.classList.toggle('on');
                  this.nextElementSibling.textContent = this.classList.contains('on') ? 'Hoạt động' : 'Ngừng hoạt động'">
    </div>
    <span class="toggle-label">${val ? 'Hoạt động' : 'Ngừng hoạt động'}</span>
  </div>`;
}
function getToggle(id) {
  return document.getElementById(id)?.classList.contains('on') ?? false;
}

// ── Lấy value input ───────────────────────────────────────────────────────────
function val(id) {
  return document.getElementById(id)?.value?.trim() ?? '';
}