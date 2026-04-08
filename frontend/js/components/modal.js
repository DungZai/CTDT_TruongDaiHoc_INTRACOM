/**
 * modal.js — Thông tin cá nhân & Đổi mật khẩu
 */

// ── Mở / đóng modal ─────────────────────────────────
function openModal(id)  { document.getElementById(id)?.classList.add('show'); }
function closeModal(id) { document.getElementById(id)?.classList.remove('show'); }

// Đóng khi click ra ngoài
document.addEventListener('click', e => {
  if (e.target.classList.contains('modal-overlay'))
    e.target.classList.remove('show');
});

// ── Mở modal thông tin cá nhân ──────────────────────
function openProfileModal() {
  const username  = TokenService.getUsername() || '—';
  const role      = TokenService.getRole()     || '—';
  const email     = TokenService.getEmail?.()  || '—';
  const createdAt = TokenService.getCreatedAt?.();

  const roleMap = { ADMIN: 'Quản trị viên', GIANG_VIEN: 'Giảng viên' };

  document.getElementById('profile-avatar').textContent  = username[0]?.toUpperCase() ?? '?';
  document.getElementById('profile-name').textContent    = username;
  document.getElementById('profile-badge').textContent   = role;
  document.getElementById('profile-username').textContent = username;
  document.getElementById('profile-email').textContent   = email;
  document.getElementById('profile-role').textContent    = roleMap[role] ?? role;
  document.getElementById('profile-created').textContent = createdAt
    ? new Date(createdAt).toLocaleDateString('vi-VN')
    : '—';

  openModal('modal-profile');
}

// ── Show / hide mật khẩu ────────────────────────────
const _eyeOpen = '<path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/>';
const _eyeOff  = '<path d="M17.94 17.94A10.07 10.07 0 0112 20c-7 0-11-8-11-8a18.45 18.45 0 015.06-5.94"/><path d="M9.9 4.24A9.12 9.12 0 0112 4c7 0 11 8 11 8a18.5 18.5 0 01-2.16 3.19"/><line x1="1" y1="1" x2="23" y2="23"/>';

function togglePw(inputId, btn) {
  const inp  = document.getElementById(inputId);
  const show = inp.type === 'password';
  inp.type   = show ? 'text' : 'password';
  btn.querySelector('svg').innerHTML = show ? _eyeOff : _eyeOpen;
  btn.style.color = show ? '#64748b' : '#cbd5e1';
}

// ── Thanh độ mạnh mật khẩu ──────────────────────────
function checkStrength(val) {
  const fill = document.getElementById('strength-fill');
  const text = document.getElementById('strength-text');
  if (!fill || !text) return;

  const hasUpper  = /[A-Z]/.test(val);
  const hasLower  = /[a-z]/.test(val);
  const hasNumber = /[0-9]/.test(val);
  const long      = val.length >= 8;

  const score = [hasUpper, hasLower, hasNumber, long].filter(Boolean).length;

  if (!val) {
    fill.className = 'strength-fill';
    text.textContent = '';
  } else if (score <= 2) {
    fill.className = 'strength-fill weak';
    text.className = 'strength-text weak';
    text.textContent = 'Độ mạnh: Yếu';
  } else if (score === 3) {
    fill.className = 'strength-fill medium';
    text.className = 'strength-text medium';
    text.textContent = 'Độ mạnh: Trung bình';
  } else {
    fill.className = 'strength-fill strong';
    text.className = 'strength-text strong';
    text.textContent = 'Độ mạnh: Mạnh';
  }
}

// ── Submit đổi mật khẩu ─────────────────────────────
async function submitChangePw() {
  const current = document.getElementById('pw-current').value.trim();
  const newPw   = document.getElementById('pw-new').value.trim();
  const confirm = document.getElementById('pw-confirm').value.trim();

  if (!current || !newPw || !confirm)
    return Toast.warning('Vui lòng nhập đầy đủ thông tin.');

  if (newPw.length < 8)
    return Toast.warning('Mật khẩu mới phải ít nhất 8 ký tự.');

  if (newPw !== confirm)
    return Toast.warning('Mật khẩu xác nhận không khớp.');

  try {
    await authApi.changePassword({ currentPassword: current, newPassword: newPw });
    Toast.success('Đổi mật khẩu thành công!');
    closeModal('modal-change-pw');
    document.getElementById('pw-current').value = '';
    document.getElementById('pw-new').value     = '';
    document.getElementById('pw-confirm').value = '';
  } catch (err) {
    Toast.error(err.message || 'Đổi mật khẩu thất bại.');
  }
}