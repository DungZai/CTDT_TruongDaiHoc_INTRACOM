/**
 * sidebar.js — Sidebar thu gọn/mở rộng
 */

async function loadSidebar() {
  try {
    const res = await fetch('/frontend/partials/sidebar.html');
    if (!res.ok) throw new Error('Không tải được sidebar');
    const html = await res.text();
    document.body.insertAdjacentHTML('afterbegin', html);
  } catch (err) {
    console.error('[Sidebar]', err);
    return;
  }
  _initSidebar();
}

function _initSidebar() {
  const sb = document.getElementById('sidebar');
  if (!sb) return;

  // ── Toggle mở/đóng ──────────────────────────────────
  const saved = localStorage.getItem('sb_open');
  if (saved === 'true') {
    sb.classList.add('open');
    document.body.classList.add('sb-open');
  }

  document.getElementById('sb-toggle')
    ?.addEventListener('click', () => {
      sb.classList.toggle('open');
      document.body.classList.toggle('sb-open');
      localStorage.setItem('sb_open', sb.classList.contains('open'));
    });

  // ── Gắn thông tin user ──────────────────────────────
  const username = TokenService.getUsername() || '—';
  const role     = TokenService.getRole()     || '—';

  const elName   = document.getElementById('sb-username');
  const elRole   = document.getElementById('sb-role');
  const elAvatar = document.getElementById('sb-avatar');

  if (elName)   elName.textContent   = username;
  if (elRole)   elRole.textContent   = _formatRoleSb(role);
  if (elAvatar) elAvatar.textContent = username[0]?.toUpperCase() ?? '?';

  // ── Hiện menu ADMIN ─────────────────────────────────
  if (role === 'ADMIN') {
    document.querySelectorAll('.sb-admin')
      .forEach(el => el.style.display = '');
  }

  // ── Active link + fade out khi chuyển trang ─────────
  const cur = window.location.pathname;
  document.querySelectorAll('.sb-link').forEach(a => {
    const href = a.getAttribute('href') || '';
    if (href && (cur.endsWith(href) || cur.includes(href.replace('.html', ''))))
      a.classList.add('active');

    a.addEventListener('click', e => {
      e.preventDefault();
      document.body.classList.add('page-leaving');
      setTimeout(() => { window.location.href = a.href; }, 200);
    });
  });

  // ── Phân quyền UI ───────────────────────────────────
  if (typeof Permission !== 'undefined') Permission.applyUI();

  // ── Logout ──────────────────────────────────────────
  document.getElementById('btn-logout')
    ?.addEventListener('click', () => authApi.logout());
}

function _formatRoleSb(role) {
  const map = {
    ADMIN      : 'Quản trị viên',
    GIANG_VIEN : 'Giảng viên',
   
  };
  return map[role] ?? role;
}