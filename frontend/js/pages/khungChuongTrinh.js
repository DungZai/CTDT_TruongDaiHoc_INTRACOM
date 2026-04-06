let _khungCtId = null, _khungEditId = null, _khungMons = [];

async function initKhung() {
  const cts = await chuongTrinhApi.getAll();
  document.getElementById('sel-khung-ct').innerHTML =
    '<option value="">-- Chọn chương trình --</option>'
    + cts.map(c => `<option value="${c.id}">${c.tenChuongTrinh}</option>`).join('');
}

async function loadKhung(ctId) {
  _khungCtId = ctId;
  const wrap    = document.getElementById('wrap-khung');
  const isAdmin = TokenService.getRole() === 'ADMIN';

  if (!ctId) { wrap.innerHTML = '<p class="text-muted mt-3">Chọn chương trình để xem khung.</p>'; return; }

  const [khung, mons] = await Promise.all([khungApi.getByChuongTrinh(ctId), monHocApi.getAll()]);
  _khungMons = mons;

  document.getElementById('inp-khung-mon').innerHTML =
    '<option value="">-- Chọn môn --</option>'
    + mons.map(m => `<option value="${m.id}">${m.maMon} — ${m.tenMon} (${m.tinChi}TC)</option>`).join('');

  const tongTC = khung.reduce((s, k) => s + (mons.find(m => m.id === k.monHocId)?.tinChi || 0), 0);
  document.getElementById('lbl-tong-tc').textContent = `Tổng: ${tongTC} TC`;

  if (!khung.length) { wrap.innerHTML = '<p class="text-muted mt-3">Chưa có môn nào trong khung.</p>'; return; }

  const byHK = {};
  khung.forEach(k => { (byHK[k.hocKy] || (byHK[k.hocKy] = [])).push(k); });

  wrap.innerHTML = Object.keys(byHK).sort((a, b) => a - b).map(hk => {
    const items = byHK[hk];
    const tcHK  = items.reduce((s, k) => s + (mons.find(m => m.id === k.monHocId)?.tinChi || 0), 0);
    const rows  = items.sort((a, b) => a.thuTu - b.thuTu).map(k => {
      const m = mons.find(x => x.id === k.monHocId);
      const actionTd = isAdmin ? `
        <td class="text-center">
          <button class="btn btn-sm btn-warning me-1" onclick="editKhung(${k.id})">Sửa</button>
          <button class="btn btn-sm btn-danger" onclick="deleteKhung(${k.id})">Xóa</button>
        </td>` : '<td></td>';
      return `<tr>
        <td>${m?.maMon || '—'}</td><td>${m?.tenMon || '—'}</td>
        <td class="text-center">${m?.tinChi || '—'}</td>
        <td>${k.loaiMon}</td><td>${k.nhomKienThuc || '—'}</td>
        ${actionTd}</tr>`;
    }).join('');

    const actionHeader = isAdmin ? '<th>Thao tác</th>' : '<th></th>';
    return `<div class="hocky-block">
      <div class="hocky-title"><span>Học kỳ ${hk}</span>
        <span class="badge" style="background:#dbeafe;color:#1e40af">${tcHK} TC</span></div>
      <div class="table-responsive"><table class="table table-sm table-bordered mb-0">
        <thead class="table-light"><tr><th>Mã môn</th><th>Tên môn</th><th>TC</th><th>Loại</th><th>Nhóm KT</th>${actionHeader}</tr></thead>
        <tbody>${rows}</tbody></table></div></div>`;
  }).join('');
}

function openKhung() {
  if (!_khungCtId) { Toast.warning('Vui lòng chọn chương trình trước.'); return; }
  _khungEditId = null;
  Modal.reset('modal-khung');
  document.getElementById('inp-khung-thutu').value = 1;
  document.getElementById('modal-khung-title').textContent = 'Thêm môn vào khung';
  Modal.open('modal-khung');
}

async function editKhung(id) {
  _khungEditId = id;
  const d = await khungApi.getById(id);
  document.getElementById('inp-khung-mon').value    = d.monHocId;
  document.getElementById('inp-khung-hk').value     = d.hocKy;
  document.getElementById('inp-khung-loai').value   = d.loaiMon;
  document.getElementById('inp-khung-nhom').value   = d.nhomKienThuc || '';
  document.getElementById('inp-khung-thutu').value  = d.thuTu || 1;
  document.getElementById('inp-khung-ghichu').value = d.ghiChu || '';
  document.getElementById('modal-khung-title').textContent = 'Sửa môn trong khung';
  Modal.open('modal-khung');
}

async function saveKhung() {
  const monId = Number(document.getElementById('inp-khung-mon').value);
  if (!monId) { Toast.warning('Vui lòng chọn môn học.'); return; }
  const body = {
    chuongTrinhId:  Number(_khungCtId),
    monHocId:       monId,
    hocKy:          Number(document.getElementById('inp-khung-hk').value),
    loaiMon:        document.getElementById('inp-khung-loai').value,
    nhomKienThuc:   document.getElementById('inp-khung-nhom').value,
    thuTu:          Number(document.getElementById('inp-khung-thutu').value) || 1,
    ghiChu:         document.getElementById('inp-khung-ghichu').value.trim(),
  };
  try {
    _khungEditId ? await khungApi.update(_khungEditId, body) : await khungApi.create(body);
    Toast.success('Lưu thành công.');
    Modal.close('modal-khung');
    loadKhung(_khungCtId);
  } catch (e) { Toast.error(e.message); }
}

async function deleteKhung(id) {
  if (!await confirmDelete('môn này khỏi khung')) return;
  try { await khungApi.delete(id); Toast.success('Đã xóa.'); loadKhung(_khungCtId); }
  catch (e) { Toast.error(e.message); }
}