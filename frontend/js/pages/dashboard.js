async function initDashboard() {
  // ── Topbar user info ──────────────────────────────────────
  const username = TokenService.getUsername();
  const role     = TokenService.getRole();

  document.getElementById('topbar-username').textContent = username || 'Admin';
  document.getElementById('topbar-role').textContent     = role || 'Quản trị viên';

  const avatarText = document.getElementById('topbar-avatar-text');
  if (avatarText) avatarText.textContent = (username || 'A').charAt(0).toUpperCase();

  document.getElementById('btn-logout-top')?.addEventListener('click', e => {
    e.preventDefault();
    authApi.logout();
  });

  document.getElementById('topbar-user-wrap')?.addEventListener('click', function(e) {
    this.classList.toggle('open');
    e.stopPropagation();
  });
  document.addEventListener('click', () => {
    document.getElementById('topbar-user-wrap')?.classList.remove('open');
  });

  // ── Greeting + ngày ──────────────────────────────────────
  const hour  = new Date().getHours();
  const greet = hour < 12 ? 'Chào buổi sáng ☀️'
              : hour < 18 ? 'Chào buổi chiều 🌤️'
              : 'Chào buổi tối 🌙';
  document.getElementById('dash-greeting').textContent = `${greet}, ${username}!`;
  document.getElementById('dash-date').textContent =
    new Date().toLocaleDateString('vi-VN', {
      weekday:'long', day:'numeric', month:'long', year:'numeric'
    });

  try {
    // ── Load song song ────────────────────────────────────
    const [nganhs, cts, mons] = await Promise.all([
      nganhApi.getAll(),
      chuongTrinhApi.getAll(),
      monHocApi.getAll(),
    ]);

    // ── Stat cards ────────────────────────────────────────
    const nganhActive = nganhs.filter(n => n.trangThai).length;
    document.getElementById('stat-nganh').textContent     = nganhs.length;
    document.getElementById('stat-nganh-sub').textContent = `${nganhActive} hoạt động`;

    document.getElementById('stat-ct').textContent     = cts.length;
    document.getElementById('stat-ct-sub').textContent =
      cts.length ? `Mới nhất: ${cts[cts.length - 1]?.namPhatHanh || ''}` : '';

    const monActive = mons.filter(m => m.trangThai).length;
    document.getElementById('stat-mon').textContent     = mons.length;
    document.getElementById('stat-mon-sub').textContent = `${monActive} hoạt động`;

    // Chuẩn đầu ra
    let totalCdr = 0;
    if (cts.length) {
      const cdrArr = await Promise.all(
        cts.map(ct => chuanDauRaApi.getByChuongTrinh(ct.id).catch(() => []))
      );
      totalCdr = cdrArr.reduce((s, arr) => s + (Array.isArray(arr) ? arr.length : 0), 0);
    }
    document.getElementById('stat-cdr').textContent     = totalCdr;
    document.getElementById('stat-cdr-sub').textContent = `Trên ${cts.length} chương trình`;

    // ── Tiến độ đề cương ──────────────────────────────────
    const dcArr   = await Promise.all(
      mons.map(m => deCuongApi.getByMonHoc(m.id).catch(() => null))
    );
    const dcCount = dcArr.filter(d => d !== null).length;
    const dcTotal = mons.length;
    const dcPct   = dcTotal ? Math.round(dcCount / dcTotal * 100) : 0;
    const circ    = 2 * Math.PI * 36;
    const offset  = circ * (1 - dcPct / 100);

    document.getElementById('progress-pct').textContent = dcPct + '%';
    document.getElementById('dc-count').textContent     = dcCount;
    document.getElementById('dc-total').textContent     = `/ ${dcTotal} môn`;
    document.getElementById('dc-missing').textContent   = `${dcTotal - dcCount} còn thiếu`;

    const bar = document.getElementById('progress-bar');
    bar.style.strokeDasharray  = circ;
    bar.style.strokeDashoffset = offset;

    // ── Phân bố tín chỉ ───────────────────────────────────
    if (cts.length) {
      const khung = await khungApi.getByChuongTrinh(cts[0].id).catch(() => []);
      if (Array.isArray(khung) && khung.length) {
        const nhomMap = {};
        khung.forEach(k => {
          const nhom = (k.nhomKienThuc || 'Khác').trim();
          nhomMap[nhom] = (nhomMap[nhom] || 0) + 1;
        });

        const total     = Object.values(nhomMap).reduce((a, b) => a + b, 0) || 1;
        const dacuong   = nhomMap['Đại cương']    || 0;
        const thuhanh      = nhomMap['Thực hành']  || 0;
        const chuyenng  = nhomMap['Chuyên ngành'] || 0;
       // const totnghiep = nhomMap['Tốt nghiệp']   || 0;

        setBar('bar-dc', 'lbl-dc', dacuong,    total);
        setBar('bar-cn', 'lbl-cn', thuhanh,    total);
        setBar('bar-tt', 'lbl-tt', chuyenng  , total);
      }
    }

    // ── Bảng CT gần đây ───────────────────────────────────
    const tbody = document.getElementById('tbody-recent-ct');
    tbody.innerHTML = [...cts].slice(-5).reverse().map(c => `
      <tr>
        <td style="font-size:12px;font-weight:600;color:#2563eb">${c.tenChuongTrinh}</td>
        <td><span class="badge-nganh">${c.tenNganh || '—'}</span></td>
        <td style="font-size:13px;font-weight:700;color:#1e293b">${c.tongTinChi}</td>
        <td style="font-size:12px;color:#64748b">${c.namPhatHanh}</td>
        <td><span class="${c.trangThai ? 'badge-active' : 'badge-hidden'}">
          ${c.trangThai ? '● Hoạt động' : '○ Ẩn'}
        </span></td>
      </tr>`).join('')
      || '<tr><td colspan="5" style="text-align:center;color:#94a3b8;padding:16px">Chưa có dữ liệu</td></tr>';

    // ── Danh sách môn học ─────────────────────────────────
    document.getElementById('list-recent-mon').innerHTML =
      [...mons].slice(-5).reverse().map(m => `
        <div class="mon-row">
          <div>
            <div class="mon-name">${m.tenMon}</div>
            <div class="mon-ma">${m.maMon}</div>
          </div>
          <span class="mon-tc">${m.tinChi} TC</span>
        </div>`).join('')
      || '<p style="color:#94a3b8;font-size:13px;text-align:center">Chưa có dữ liệu</p>';

    // ── Hiện nhóm Quản trị nếu là ADMIN ──────────────────
    if (role === 'ADMIN') {
      document.getElementById('wrap-admin-sep').style.display   = '';
      document.getElementById('wrap-admin-label').style.display = '';
      document.getElementById('wrap-admin-links').style.display = '';
    }

  } catch (e) {
    Toast.error('Không thể tải dữ liệu: ' + e.message);
  }
}

function setBar(barId, lblId, val, total) {
  const pct  = total ? Math.round(val / total * 100) : 0;
  const fill = document.getElementById(barId);
  const lbl  = document.getElementById(lblId);
  if (fill) fill.style.width = pct + '%';
  if (lbl)  lbl.textContent  = val + ' môn';
}