let _userEditId = null, _userRoles = [];

async function loadUsers() {
  _userRoles = await userApi.getRoles();
  document.getElementById('inp-user-role').innerHTML =
    '<option value="">-- Chọn vai trò --</option>'
    + _userRoles.map(r => `<option value="${r.id}">${r.role_name}</option>`).join('');

  const users = await userApi.getAll();
  renderTable({
    tbodyId: 'tbody-users',
    columns: [
      { key: 'username' },
      { key: 'email',    render: r => r.email || '—' },
      { key: 'role_id',  render: r => {
        const role = _userRoles.find(x => x.id === r.role_id);
        return role ? `<span class="badge" style="background:#dbeafe;color:#1e40af">${role.role_name}</span>` : '—';
      }},
      { key: 'created_at', render: r => Formatter.date(r.created_at) },
    ],
    data: users,
    actions: { edit: 'editUser', delete: 'deleteUser' },
  });
}

function openUser() {
  _userEditId = null;
  Modal.reset('modal-user');
  document.getElementById('wrap-user-pw').style.display = '';
  document.getElementById('modal-user-title').textContent = 'Thêm tài khoản';
  Modal.open('modal-user');
}

async function editUser(id) {
  _userEditId = id;
  const d = await userApi.getById(id);
  document.getElementById('inp-username').value   = d.username;
  document.getElementById('inp-user-email').value = d.email || '';
  document.getElementById('inp-user-role').value  = d.role_id;
  document.getElementById('wrap-user-pw').style.display = 'none';
  document.getElementById('modal-user-title').textContent = 'Sửa tài khoản';
  Modal.open('modal-user');
}

async function saveUser() {
  const roleId = Number(document.getElementById('inp-user-role').value);
  const body   = {
    username: document.getElementById('inp-username').value.trim(),
    email:    document.getElementById('inp-user-email').value.trim(),
    role_id:  roleId,
  };
  if (!_userEditId) {
    body.password = document.getElementById('inp-user-pw').value;
    if (!body.password) { Toast.warning('Vui lòng nhập mật khẩu.'); return; }
  }
  if (!Validator.required(body.username, 'Username')) return;
  if (!roleId) { Toast.warning('Vui lòng chọn vai trò.'); return; }
  try {
    _userEditId ? await userApi.update(_userEditId, body) : await userApi.create(body);
    Toast.success('Lưu thành công.');
    Modal.close('modal-user');
    loadUsers();
  } catch (e) { Toast.error(e.message); }
}

async function deleteUser(id) {
  if (!await confirmDelete('tài khoản này')) return;
  try { await userApi.delete(id); Toast.success('Xóa thành công.'); loadUsers(); }
  catch (e) { Toast.error(e.message); }
}