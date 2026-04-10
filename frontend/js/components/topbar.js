function initTopbar() {
  if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', _bindTopbar);
  } else {
    _bindTopbar();
  }
}

async function loadTopbar() {
  const basePath = '../components/topbar.html';
  try {
    const res = await fetch(basePath);
    if (!res.ok) throw new Error(`Không tải được topbar (${res.status})`);
    const html = await res.text();
    document.body.insertAdjacentHTML('afterbegin', html);
  } catch (err) {
    console.error('[Topbar]', err);
    return;
  }
  _bindTopbar();
}

function _bindTopbar() {
  const username = TokenService.getUsername?.() || '—';
  const role     = TokenService.getRole?.()     || '—';

  const elName   = document.getElementById('topbar-username');
  const elRole   = document.getElementById('topbar-role');
  const elAvatar = document.getElementById('topbar-avatar-text');

  if (elName)   elName.textContent   = username;
  if (elRole)   elRole.textContent   = _formatRole(role);
  if (elAvatar) elAvatar.textContent = _getInitial(username);

  const subbar = document.getElementById('topbar-subbar');
  if (subbar) {
    const isDashboard = window.location.pathname.endsWith('dashboard.html');
    subbar.classList.toggle('hidden', isDashboard);
  }

  document.getElementById('btn-logout-top')
    ?.addEventListener('click', async (e) => {
      e.preventDefault();
      try { await authApi.logout?.(); } catch (_) {}
      TokenService.clear?.();
      window.location.href = '../pages/login.html';
    });

  const userWrap = document.getElementById('topbar-user-wrap');
  if (userWrap) {
    userWrap.addEventListener('click', e => {
      const item = e.target.closest('.dropdown-item-custom');
      if (item) {
        userWrap.classList.remove('open');
        const text = item.textContent.trim();
        if (text.includes('Thông tin cá nhân')) {
          e.preventDefault();
          openProfileModal();
        } else if (text.includes('Đổi mật khẩu')) {
          e.preventDefault();
          openModal('modal-change-pw');
        }
        return;
      }
      userWrap.classList.toggle('open');
    });

    document.addEventListener('mousedown', e => {
      if (!userWrap.contains(e.target)) {
        userWrap.classList.remove('open');
      }
    });
  }
}

function _getInitial(name) {
  const parts = name.trim().split(/\s+/);
  return parts[parts.length - 1]?.[0]?.toUpperCase() ?? '?';
}

function _formatRole(role) {
  const map = {
    ADMIN      : 'Quản trị viên',
    GIANG_VIEN : 'Giảng viên',
  };
  return map[role] ?? role;
}

function _updateBellBadge(count) {
  const badge = document.getElementById('bell-badge');
  if (!badge) return;
  if (count > 0) {
    badge.textContent   = count > 99 ? '99+' : count;
    badge.style.display = 'flex';
  } else {
    badge.style.display = 'none';
  }
}

document.addEventListener('click', e => {
  const item = e.target.closest('.dropdown-item-custom');
  if (!item) return;
  const text = item.textContent.trim();
  if (text.includes('Thông tin cá nhân')) {
    e.preventDefault();
    openProfileModal();
  } else if (text.includes('Đổi mật khẩu')) {
    e.preventDefault();
    openModal('modal-change-pw');
  }
});