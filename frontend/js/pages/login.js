if (TokenService.isValid()) {
  window.location.href = '/frontend/pages/dashboard.html';
}

async function handleLogin(e) {
  e.preventDefault();
  const username = document.getElementById('username').value.trim();
  const password = document.getElementById('password').value;
  if (!username || !password) { Toast.warning('Vui lòng nhập đầy đủ thông tin.'); return; }
  try {
    await authApi.login(username, password);
    window.location.href = '/frontend/pages/dashboard.html';
  } catch (err) {
    Toast.error('Tài khoản hoặc mật khẩu không đúng.');
  }
}