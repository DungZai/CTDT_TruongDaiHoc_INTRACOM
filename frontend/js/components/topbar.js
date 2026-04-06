/**
 * topbar.js
 * Load topbar HTML vào đầu <body>, gắn thông tin user và xử lý logout.
 *
 * Yêu cầu: token.js và authApi.js phải được load trước file này.
 *
 * Cách dùng trong mỗi trang:
 *   await loadTopbar();
 */

/**
 * Dùng cho dashboard.html — topbar HTML đã có sẵn, chỉ cần gắn data + logout
 */
function initTopbar() {
  _bindTopbar();
}

/**
 * Dùng cho các trang con — fetch HTML rồi mới gắn data + logout
 */
async function loadTopbar() {
  // ── 1. Xác định đường dẫn tương đối đến components/ ──────────────────────
  // Tất cả pages/ đều nằm cùng cấp → dùng cùng path
  const basePath = '../components/topbar.html';

  // ── 2. Fetch và chèn HTML ─────────────────────────────────────────────────
  try {
    const res = await fetch(basePath);
    if (!res.ok) throw new Error(`Không tải được topbar (${res.status})`);
    const html = await res.text();
    document.body.insertAdjacentHTML('afterbegin', html);
  } catch (err) {
    console.error('[Topbar]', err);
    return; // Không block trang nếu topbar lỗi
  }

  // ── 3. Gắn thông tin user + logout ───────────────────────────────────────
  _bindTopbar();
}

// ── Shared bind (dùng chung cho cả initTopbar và loadTopbar) ─────────────────
function _bindTopbar() {
  const user     = TokenService.getUser?.() ?? {};
  const fullName = user.fullName ?? user.name ?? user.username ?? '—';
  const role     = user.role ?? TokenService.getRole?.() ?? '—';

  const elName   = document.getElementById('topbar-username');
  const elRole   = document.getElementById('topbar-role');
  const elAvatar = document.getElementById('topbar-avatar-text');

  if (elName)   elName.textContent   = fullName;
  if (elRole)   elRole.textContent   = _formatRole(role);
  if (elAvatar) elAvatar.textContent = _getInitial(fullName);

  // Ẩn subbar nếu đang ở dashboard, hiện nếu ở trang con
  const subbar = document.getElementById('topbar-subbar');
  if (subbar) {
    const isDashboard = window.location.pathname.endsWith('dashboard.html');
    subbar.classList.toggle('hidden', isDashboard);
  }

  document.getElementById('btn-logout-top')
    ?.addEventListener('click', async (e) => {
      e.preventDefault();
      try { await AuthApi.logout?.(); } catch (_) { /* ignore */ }
      TokenService.clear?.();
      window.location.href = '../pages/login.html';
    });
}

// ── Helpers ───────────────────────────────────────────────────────────────────

/** Lấy chữ cái đầu của tên để hiển thị avatar */
function _getInitial(name) {
  const parts = name.trim().split(/\s+/);
  return parts[parts.length - 1]?.[0]?.toUpperCase() ?? '?';
}

/** Dịch role code sang tiếng Việt */
function _formatRole(role) {
  const map = {
    ADMIN       : 'Quản trị viên',
    GIANG_VIEN  : 'Giảng viên',
    SINH_VIEN   : 'Sinh viên',
  };
  return map[role] ?? role;
}

/** Cập nhật badge số thông báo (gọi khi có dữ liệu) */
function _updateBellBadge(count) {
  const badge = document.getElementById('bell-badge');
  if (!badge) return;
  if (count > 0) {
    badge.textContent    = count > 99 ? '99+' : count;
    badge.style.display  = 'flex';
  } else {
    badge.style.display  = 'none';
  }
}