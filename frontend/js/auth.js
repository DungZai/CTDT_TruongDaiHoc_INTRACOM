// ── Kiểm tra đăng nhập ────────────────────────────────────────────────────────
function requireAuth() {
  const token = localStorage.getItem('token');
  if (!token) {
    // Xác định đúng đường dẫn về trang login tuỳ vị trí hiện tại
    const isInPages = window.location.pathname.includes('/pages/');
    window.location.href = isInPages ? '../index.html' : 'index.html';
    return false;
  }
  return true;
}

// ── Đăng xuất ─────────────────────────────────────────────────────────────────
function logout() {
  if (!confirm('Bạn có chắc muốn đăng xuất?')) return;
  localStorage.clear();
  window.location.href = '/index.html';
}

// ── Lấy thông tin user đang đăng nhập ────────────────────────────────────────
function getCurrentUser() {
  const u = localStorage.getItem('user');
  return u ? JSON.parse(u) : null;
}

// ── Hiển thị thông tin user trên topbar ──────────────────────────────────────
function renderUserInfo() {
  const user = getCurrentUser();
  if (!user) return;
  const el = document.getElementById('user-info');
  if (el) {
    el.innerHTML = `
      <span style="font-size:13px;color:#64748b">${user.username}</span>
      <span class="badge badge-blue" style="font-size:11px">${user.role || ''}</span>
    `;
  }
}

// ── Kiểm tra quyền Admin ─────────────────────────────────────────────────────
function isAdmin() {
  const user = getCurrentUser();
  return user?.role === 'ADMIN';
}