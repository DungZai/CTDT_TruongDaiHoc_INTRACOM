let _userEditId = null, _userRoles = [], _bsModal = null;

async function loadUsers() {
  _userRoles = await userApi.getRoles();

  // Chỉ hiện ADMIN và GIANG_VIEN
  const allowed = ['ADMIN', 'GIANG_VIEN'];
  document.getElementById('inp-user-role').innerHTML =
    '<option value="">-- Chọn vai trò --</option>'
    + _userRoles
        .filter(r => allowed.includes(r.roleName))
        .map(r => `<option value="${r.id}">${r.roleName}</option>`).join('');

  const users = await userApi.getAll();
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
    data: users,
    actions: { edit: 'editUser', delete: 'deleteUser' },
  });
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
  document.getElementById('wrap-user-pw').style.display = '';
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
  document.getElementById('wrap-user-pw').style.display  = 'none';
  document.getElementById('inp-username').disabled       = false; // cho phép sửa username
  _getModal().show();
}

async function saveUser() {
  const roleId = Number(document.getElementById('inp-user-role').value);
  if (!roleId) { Toast.warning('Vui lòng chọn vai trò.'); return; }

  try {
    if (_userEditId) {
      await userApi.update(_userEditId, {
        username: document.getElementById('inp-username').value.trim(),
        email:    document.getElementById('inp-user-email').value.trim(),
        roleId,
      });
    } else {
      const username = document.getElementById('inp-username').value.trim();
      const password = document.getElementById('inp-user-pw').value;
      const email    = document.getElementById('inp-user-email').value.trim();
      if (!username) { Toast.warning('Vui lòng nhập username.'); return; }
      if (!password) { Toast.warning('Vui lòng nhập mật khẩu.'); return; }
      await userApi.create({ username, password, email, roleId });
    }
    Toast.success('Lưu thành công.');
    _getModal().hide();
    loadUsers();
  } catch (e) { Toast.error(e.message); }
}

async function deleteUser(id) {
  if (!await confirmDelete('tài khoản này')) return;
  try {
    await userApi.delete(id);
    Toast.success('Xóa thành công.');
    loadUsers();
  } catch (e) { Toast.error(e.message); }
}