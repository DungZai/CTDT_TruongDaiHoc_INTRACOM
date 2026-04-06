let _userEditId = null, _userRoles = [];

async function loadUsers() {
  _userRoles = await userApi.getRoles();
  document.getElementById('inp-user-role').innerHTML =
    '<option value="">-- Chọn vai trò --</option>'
    + _userRoles.map(r => `<option value="${r.id}">${r.roleName}</option>`).join('');

  const users = await userApi.getAll();
  renderTable({
    tbodyId: 'tbody-users',
    columns: [
      { key: 'username' },
      { key: 'email',     render: r => r.email || '—' },
      { key: 'roleName', render: r =>
        r.roleName ? `<span class="badge" style="background:#dbeafe;color:#1e40af">${r.roleName}</span>` : '—'
      },
      { key: 'createdAt', render: r => Formatter.date(r.createdAt) },
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
  document.getElementById('inp-user-role').value  = d.roleId;
  document.getElementById('wrap-user-pw').style.display = 'none';
  document.getElementById('modal-user-title').textContent = 'Sửa tài khoản';
  Modal.open('modal-user');
}

async function saveUser() {
  const roleId = Number(document.getElementById('inp-user-role').value);
  if (!Validator.required(document.getElementById('inp-username').value.trim(), 'Username')) return;
  if (!roleId) { Toast.warning('Vui lòng chọn vai trò.'); return; }

  try {
    if (_userEditId) {
      // Chỉ gửi email + roleId khi update
      const body = {
        email:  document.getElementById('inp-user-email').value.trim(),
        roleId,
      };
      await userApi.update(_userEditId, body);
    } else {
      // Gửi đầy đủ khi tạo mới
      const password = document.getElementById('inp-user-pw').value;
      if (!password) { Toast.warning('Vui lòng nhập mật khẩu.'); return; }
      const body = {
        username: document.getElementById('inp-username').value.trim(),
        email:    document.getElementById('inp-user-email').value.trim(),
        password,
        roleId,
      };
      await userApi.create(body);
    }
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