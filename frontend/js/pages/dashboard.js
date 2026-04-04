document.addEventListener('DOMContentLoaded', async () => {
  if (!requireAuth()) return;
  renderUserInfo();
  loadSidebar();
  await loadStats();
  await loadMonHoc();
  await loadChuongTrinh();
});

async function loadStats() {
  try {
    const [nganh, he, ct, mon, cdr, users] = await Promise.all([
      api.get('/api/nganh'),
      api.get('/api/he-dao-tao'),
      api.get('/api/chuong-trinh'),
      api.get('/api/mon-hoc'),
      api.get('/api/chuan-dau-ra/by-chuong-trinh/1'),
      api.get('/api/users'),
    ]);
    document.getElementById('cnt-nganh').textContent = (nganh||[]).length;
    document.getElementById('cnt-he').textContent    = (he||[]).length;
    document.getElementById('cnt-ct').textContent    = (ct||[]).length;
    document.getElementById('cnt-mon').textContent   = (mon||[]).length;
    document.getElementById('cnt-cdr').textContent   = (cdr||[]).length;
    document.getElementById('cnt-user').textContent  = (users||[]).length;
  } catch(e) { console.error(e); }
}

async function loadMonHoc() {
  try {
    const data = await api.get('/api/mon-hoc');
    const rows = (data||[]).slice(0,8);
    document.getElementById('tbl-monhoc').innerHTML = rows.length
      ? rows.map(m => `<tr>
          <td><strong>${m.maMon}</strong></td>
          <td>${m.tenMon}</td>
          <td>${m.tinChi}</td>
          <td>${m.soTietLt ?? '--'}</td>
          <td>${m.soTietTh ?? '--'}</td>
          <td><span class="badge ${m.trangThai ? 'badge-green':'badge-red'}">${m.trangThai?'Hoạt động':'Ngừng'}</span></td>
        </tr>`).join('')
      : '<tr><td colspan="6" class="table-empty">Không có dữ liệu</td></tr>';
  } catch(e) { console.error(e); }
}

async function loadChuongTrinh() {
  try {
    const data = await api.get('/api/chuong-trinh');
    document.getElementById('tbl-ct').innerHTML = (data||[]).length
      ? (data||[]).map(c => `<tr>
          <td><strong>${c.tenChuongTrinh}</strong></td>
          <td>${c.tenNganh ?? '--'}</td>
          <td>${c.tenHe ?? '--'}</td>
          <td>${c.tongTinChi}</td>
          <td>${c.namPhatHanh}</td>
          <td><span class="badge ${c.trangThai?'badge-green':'badge-red'}">${c.trangThai?'Hoạt động':'Ngừng'}</span></td>
        </tr>`).join('')
      : '<tr><td colspan="6" class="table-empty">Không có dữ liệu</td></tr>';
  } catch(e) { console.error(e); }
}