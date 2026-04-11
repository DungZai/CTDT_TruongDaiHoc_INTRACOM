// ── Hàm nội bộ phân biệt 2 loại modal ──
function _isBootstrapModal(el) {
  return el.classList.contains('modal');
}

// ── openModal / closeModal dùng chung ──
function openModal(id) {
  const el = document.getElementById(id);
  if (!el) return;
  if (_isBootstrapModal(el)) {
    const old = bootstrap.Modal.getInstance(el);
    if (old) old.dispose();
    new bootstrap.Modal(el).show();
  } else {
    el.classList.add('show');
  }
}

function closeModal(id) {
  const el = document.getElementById(id);
  if (!el) return;
  if (_isBootstrapModal(el)) {
    bootstrap.Modal.getInstance(el)?.hide();
  } else {
    el.classList.remove('show');
  }
}

// ── Click outside để đóng modal CSS thuần ──
document.addEventListener('click', e => {
  if (e.target.classList.contains('modal-overlay'))
    e.target.classList.remove('show');
});

// ── Profile modal ──
function openProfileModal() {
  const user    = JSON.parse(localStorage.getItem('userInfo') || '{}');
  const roleMap = { ADMIN: 'Quản trị viên', GIANG_VIEN: 'Giảng viên' };
  const set = (id, val) => { const el = document.getElementById(id); if (el) el.textContent = val; };
  set('profile-avatar',   user.username?.[0]?.toUpperCase() ?? '?');
  set('profile-name',     user.username  || '—');
  set('profile-badge',    user.roleName  || '—');
  set('profile-username', user.username  || '—');
  set('profile-email',    user.email     || '—');
  set('profile-role',     (roleMap[user.roleName] ?? user.roleName) ?? '—');
  openModal('modal-profile'); // modal-overlay → dùng CSS thuần
}

// ── Toggle mật khẩu ──
var MODAL_EYE_OPEN = '<path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"/><circle cx="12" cy="12" r="3"/>';
var MODAL_EYE_OFF  = '<path d="M17.94 17.94A10.07 10.07 0 0112 20c-7 0-11-8-11-8a18.45 18.45 0 015.06-5.94"/><path d="M9.9 4.24A9.12 9.12 0 0112 4c7 0 11 8 11 8a18.5 18.5 0 01-2.16 3.19"/><line x1="1" y1="1" x2="23" y2="23"/>';

function togglePw(inputId, btn) {
  const inp  = document.getElementById(inputId);
  const show = inp.type === 'password';
  inp.type   = show ? 'text' : 'password';
  btn.querySelector('svg').innerHTML = show ? MODAL_EYE_OFF : MODAL_EYE_OPEN;
  btn.style.color = show ? '#64748b' : '#cbd5e1';
}

// ── Kiểm tra độ mạnh mật khẩu ──
function checkStrength(val) {
  const fill = document.getElementById('strength-fill');
  const text = document.getElementById('strength-text');
  if (!fill || !text) return;
  const score = [/[A-Z]/.test(val), /[a-z]/.test(val), /[0-9]/.test(val), val.length >= 8].filter(Boolean).length;
  if (!val) {
    fill.className = 'strength-fill'; text.textContent = '';
  } else if (score <= 2) {
    fill.className = 'strength-fill weak'; text.className = 'strength-text weak'; text.textContent = 'Độ mạnh: Yếu';
  } else if (score === 3) {
    fill.className = 'strength-fill medium'; text.className = 'strength-text medium'; text.textContent = 'Độ mạnh: Trung bình';
  } else {
    fill.className = 'strength-fill strong'; text.className = 'strength-text strong'; text.textContent = 'Độ mạnh: Mạnh';
  }
}

// ── Đổi mật khẩu ──
async function submitChangePw() {
  const current = document.getElementById('pw-current').value.trim();
  const newPw   = document.getElementById('pw-new').value.trim();
  const confirm = document.getElementById('pw-confirm').value.trim();
  if (!current || !newPw || !confirm) return Toast.warning('Vui lòng nhập đầy đủ thông tin.');
  if (newPw.length < 8) return Toast.warning('Mật khẩu mới phải ít nhất 8 ký tự.');
  if (newPw !== confirm) return Toast.warning('Mật khẩu xác nhận không khớp.');
  try {
    await authApi.changePassword({ currentPassword: current, newPassword: newPw });
    Toast.success('Đổi mật khẩu thành công!');
    closeModal('modal-change-pw');
    ['pw-current', 'pw-new', 'pw-confirm'].forEach(id => document.getElementById(id).value = '');
  } catch (err) {
    Toast.error(err.message || 'Đổi mật khẩu thất bại.');
  }
}

// ── Object Modal — dùng cho các trang data (Bootstrap modal) ──
var Modal = {
  open(id) {
    const el = document.getElementById(id);
    if (!el) return;
    const old = bootstrap.Modal.getInstance(el);
    if (old) old.dispose();
    new bootstrap.Modal(el).show();
  },
  close(id) {
    const el = document.getElementById(id);
    if (!el) return;
    bootstrap.Modal.getInstance(el)?.hide();
  },
  reset(id) {
    const el = document.getElementById(id);
    if (!el) return;
    el.querySelectorAll('input:not([type=hidden]), textarea, select').forEach(f => {
      if (f.type === 'checkbox' || f.type === 'radio') f.checked = false;
      else f.value = '';
    });
  }
};

// ── Fix aria-hidden warning ──
document.addEventListener('hide.bs.modal', e => {
  const focused = e.target.querySelector(':focus');
  if (focused) focused.blur();
});