let _userEditId = null, _userRoles = [], _bsModal = null;

window._userKeyword = '';

async function loadUsers(page = 0, size = 10) {
  // Load roles 1 lần
  if (!_userRoles.length) {
    _userRoles = await userApi.getRoles();
    const allowed = ['ADMIN', 'GIANG_VIEN'];
    document.getElementById('inp-user-role').innerHTML =
      '<option value="">-- Chọn vai trò --</option>'
      + _userRoles
          .filter(r => allowed.includes(r.roleName))
          .map(r => `<option value="${r.id}">${r.roleName}</option>`).join('');
  }

  const params = new URLSearchParams({ page, size });
  if (window._userKeyword) params.append('keyword', window._userKeyword);

  const res = await userApi.getAll(params.toString());

  renderTable({
    tbodyId: 'tbody-users',
    columns: [
      { key: 'username' },
      { key: 'email',     render: r => r.email || '—' },
      { key: 'roleName',  render: r => r.roleName
          ? `<span class="badge" style="background:#dbeafe;color:#1e40af">${r.roleName}</span>`
          : '—' },
      { key: 'createdAt', render: r => Formatter.date(r.createdAt) },
    ],
    data:       res.content,
    actions:    { edit: 'editUser', delete: 'deleteUser' },
    pageOffset: res.page * res.size,
  });

  Pagination.update('pagination-users', res);
}

function _getModal() {
  if (!_bsModal) _bsModal = new bootstrap.Modal(document.getElementById('modal-user'));
  return _bsModal;
}

function openUser() {
  _userEditId = null;
  document.getElementById('modal-user-title').textContent = 'Thêm tài khoản';
  document.getElementById('inp-username').value   = '';
  document.getElementById('inp-user-pw').value    = '';
  document.getElementById('inp-user-email').value = '';
  document.getElementById('inp-user-role').value  = '';
  
  // Hiện password field (bắt buộc khi thêm mới)
  document.getElementById('wrap-user-pw').style.display = '';
  document.getElementById('pw-required').style.display = 'inline'; // Hiện dấu *
  document.getElementById('pw-hint').style.display = 'none'; // Ẩn hint
  document.getElementById('inp-user-pw').placeholder = '';
  
  document.getElementById('inp-username').disabled = false;
  _getModal().show();
}

async function editUser(id) {
  _userEditId = id;
  const d = await userApi.getById(id);
  document.getElementById('modal-user-title').textContent = 'Sửa tài khoản';
  document.getElementById('inp-username').value   = d.username;
  document.getElementById('inp-user-email').value = d.email || '';
  document.getElementById('inp-user-role').value  = _userRoles.find(r => r.roleName === d.roleName)?.id || '';
  
  // Hiện password field (không bắt buộc khi sửa)
  document.getElementById('wrap-user-pw').style.display  = ''; // HIỆN ra thay vì ẩn
  document.getElementById('inp-user-pw').value = ''; // Clear password
  document.getElementById('pw-required').style.display = 'none'; // Ẩn dấu *
  document.getElementById('pw-hint').style.display = 'block'; // Hiện hint
  document.getElementById('inp-user-pw').placeholder = 'Nhập mật khẩu mới nếu muốn đổi...';
  
  document.getElementById('inp-username').disabled = false;
  _getModal().show();
}

async function saveUser() {
  const roleId = Number(document.getElementById('inp-user-role').value);
  if (!roleId) { Toast.warning('Vui lòng chọn vai trò.'); return; }

  try {
    if (_userEditId) {
      // === EDIT MODE ===
      const username = document.getElementById('inp-username').value.trim();
      const email = document.getElementById('inp-user-email').value.trim();
      const newPassword = document.getElementById('inp-user-pw').value.trim();

      // 1. Update thông tin cơ bản
      await userApi.update(_userEditId, { username, email, roleId });

      // 2. Nếu có nhập mật khẩu mới → reset password
      if (newPassword) {
        if (newPassword.length < 6) {
          Toast.warning('Mật khẩu phải có ít nhất 6 ký tự.');
          return;
        }
        await userApi.resetPassword(_userEditId, { newPassword });
      }

      Toast.success('Cập nhật thành công.');
    } else {
      // === CREATE MODE ===
      const username = document.getElementById('inp-username').value.trim();
      const password = document.getElementById('inp-user-pw').value;
      const email = document.getElementById('inp-user-email').value.trim();

      if (!username) { Toast.warning('Vui lòng nhập username.'); return; }
      if (!password) { Toast.warning('Vui lòng nhập mật khẩu.'); return; }
      if (password.length < 6) { Toast.warning('Mật khẩu phải có ít nhất 6 ký tự.'); return; }

      await userApi.create({ username, password, email, roleId });
      Toast.success('Thêm tài khoản thành công.');
    }

    _getModal().hide();
    loadUsers(Pagination.getPage('pagination-users'), Pagination.getSize('pagination-users'));
  } catch (e) { 
    Toast.error(e.message); 
  }
}

async function deleteUser(id) {
  if (!await confirmDelete('tài khoản này')) return;
  try {
    await userApi.delete(id);
    Toast.success('Xóa thành công.');
    const cur = Pagination.getPage('pagination-users');
    loadUsers(cur > 0 ? cur - 1 : 0, Pagination.getSize('pagination-users'));
  } catch (e) { Toast.error(e.message); }
}