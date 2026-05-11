async function loadSidebar() {
  try {
    const res = await fetch('/partials/sidebar.html');
    if (!res.ok) throw new Error('Không tải được sidebar');
    const html = await res.text();
    document.body.insertAdjacentHTML('afterbegin', html); // ✅ dòng duy nhất
  } catch (err) {
    console.error('[Sidebar]', err);
    return;
  }
  _initSidebar();
}

function loadModals() {
  const el = document.createElement('div');
  el.id = 'modals-root';
  el.innerHTML = `
    <form autocomplete="off" style="display:contents">

    <div class="modal-overlay" id="modal-profile">
      <div class="modal-box">
        <div class="modal-header">
          <div class="modal-title">Thông tin cá nhân</div>
          <button class="modal-close" onclick="closeModal('modal-profile')">✕</button>
        </div>
        <div class="modal-body">
          <div class="profile-top">
            <div class="profile-avatar" id="profile-avatar">?</div>
            <div class="profile-name" id="profile-name">—</div>
            <div class="profile-badge" id="profile-badge">—</div>
          </div>
          <div class="profile-divider"></div>
          <div class="info-row">
            <div class="info-icon"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2"/><circle cx="12" cy="7" r="4"/></svg></div>
            <div><div class="info-label">Tên đăng nhập</div><div class="info-value" id="profile-username">—</div></div>
          </div>
          <div class="info-row">
            <div class="info-icon"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/><polyline points="22,6 12,13 2,6"/></svg></div>
            <div><div class="info-label">Email</div><div class="info-value" id="profile-email">—</div></div>
          </div>
          <div class="info-row">
            <div class="info-icon"><svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.8"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z"/></svg></div>
            <div><div class="info-label">Vai trò</div><div class="info-value" id="profile-role">—</div></div>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn-modal-close" onclick="closeModal('modal-profile')">Đóng</button>
        </div>
      </div>
    </div>

    <div class="modal-overlay" id="modal-change-pw">
      <div class="modal-box">
        <div class="modal-header">
          <div class="modal-title">Đổi mật khẩu</div>
          <button class="modal-close" onclick="closeModal('modal-change-pw')">✕</button>
        </div>
        <div class="modal-body">
          <div class="pw-group">
            <label class="pw-label">Mật khẩu hiện tại</label>
            <div class="pw-wrap">
              <input class="pw-input" type="password" id="pw-current" autocomplete="new-password" placeholder="Nhập mật khẩu hiện tại">
            </div>
          </div>
          <div class="pw-section">Mật khẩu mới</div>
          <div class="pw-group">
            <label class="pw-label">Mật khẩu mới</label>
            <div class="pw-wrap">
              <input class="pw-input" type="password" id="pw-new" autocomplete="new-password" placeholder="Nhập mật khẩu mới" oninput="checkStrength(this.value)">
            </div>
            <div class="strength-bar"><div class="strength-fill" id="strength-fill"></div></div>
            <div class="strength-text" id="strength-text"></div>
          </div>
          <div class="pw-group">
            <label class="pw-label">Xác nhận mật khẩu mới</label>
            <div class="pw-wrap">
              <input class="pw-input" type="password" id="pw-confirm" autocomplete="new-password" placeholder="Nhập lại mật khẩu mới">
            </div>
          </div>
          <div class="pw-warn">⚠️ Mật khẩu tối thiểu 8 ký tự, gồm chữ hoa, chữ thường và số.</div>
        </div>
        <div class="modal-footer">
          <button class="btn-modal-close" onclick="closeModal('modal-change-pw')">Hủy</button>
          <button class="btn-modal-save" onclick="submitChangePw()">Đổi mật khẩu</button>
        </div>
      </div>
    </div>

    </form>
  `;
  document.body.appendChild(el);
}



function _initSidebar() {
  const sb = document.getElementById('sidebar');
  if (!sb) return;

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

  const username = TokenService.getUsername() || '—';
  const role     = TokenService.getRole()     || '—';

  const elName   = document.getElementById('sb-username');
  const elRole   = document.getElementById('sb-role');
  const elAvatar = document.getElementById('sb-avatar');

  if (elName)   elName.textContent   = username;
  if (elRole)   elRole.textContent   = _formatRoleSb(role);
  if (elAvatar) elAvatar.textContent = username[0]?.toUpperCase() ?? '?';

  if (role === 'ADMIN') {
    document.querySelectorAll('.sb-admin')
      .forEach(el => el.style.display = '');
  }

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

  if (typeof Permission !== 'undefined') Permission.applyUI();

  document.getElementById('btn-logout')
    ?.addEventListener('click', () => authApi.logout());
}

function _formatRoleSb(role) {
  const map = { ADMIN: 'Quản trị viên', GIANG_VIEN: 'Giảng viên' };
  return map[role] ?? role;
}

document.addEventListener('DOMContentLoaded', loadModals);
