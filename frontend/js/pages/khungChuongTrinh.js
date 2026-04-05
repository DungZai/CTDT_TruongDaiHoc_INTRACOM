let _khungCtId = null, _khungEditId = null, _khungMons = [];

async function initKhung() {
  const cts = await chuongTrinhApi.getAll();
  document.getElementById('sel-khung-ct').innerHTML =
    '<option value="">-- Chọn chương trình --</option>'
    + cts.map(c => `<option value="${c.id}">${c.ten_chuong_trinh}</option>`).join('');
}

async function loadKhung(ctId) {
  _khungCtId = ctId;
  const wrap = document.getElementById('wrap-khung');
  if (!ctId) { wrap.innerHTML = '<p class="text-muted mt-3">Chọn chương trình để xem khung.</p>'; return; }

  const [khung, mons] = await Promise.all([khungApi.getByChuongTrinh(ctId), monHocApi.getAll()]);
  _khungMons = mons;

  document.getElementById('inp-khung-mon').innerHTML =
    '<option value="">-- Chọn môn --</option>'
    + mons.map(m => `<option value="${m.id}">${m.ma_mon} — ${m.ten_mon} (${m.tin_chi}TC)</option>`).join('');

  const tongTC = khung.reduce((s, k) => s + (mons.find(m => m.id === k.mon_hoc_id)?.tin_chi || 0), 0);
  document.getElementById('lbl-tong-tc').textContent = `Tổng: ${tongTC} TC`;

  if (!khung.length) { wrap.innerHTML = '<p class="text-muted mt-3">Chưa có môn nào trong khung.</p>'; return; }

  const byHK = {};
  khung.forEach(k => { (byHK[k.hoc_ky] || (byHK[k.hoc_ky] = [])).push(k); });

  wrap.innerHTML = Object.keys(byHK).sort((a, b) => a - b).map(hk => {
    const items = byHK[hk];
    const tcHK  = items.reduce((s, k) => s + (mons.find(m => m.id === k.mon_hoc_id)?.tin_chi || 0), 0);
    const rows  = items.sort((a, b) => a.thu_tu - b.thu_tu).map(k => {
      const m = mons.find(x => x.id === k.mon_hoc_id);
      return `<tr>
        <td>${m?.ma_mon || '—'}</td><td>${m?.ten_mon || '—'}</td>
        <td class="text-center">${m?.tin_chi || '—'}</td>
        <td>${k.loai_mon}</td><td>${k.nhom_kien_thuc || '—'}</td>
        <td class="text-center">
          <button class="btn btn-sm btn-warning me-1" onclick="editKhung(${k.id})">Sửa</button>
          <button class="btn btn-sm btn-danger" onclick="deleteKhung(${k.id})">Xóa</button>
        </td></tr>`;
    }).join('');
    return `<div class="hocky-block">
      <div class="hocky-title"><span>Học kỳ ${hk}</span>
        <span class="badge" style="background:#dbeafe;color:#1e40af">${tcHK} TC</span></div>
      <div class="table-responsive"><table class="table table-sm table-bordered mb-0">
        <thead class="table-light"><tr><th>Mã môn</th><th>Tên môn</th><th>TC</th><th>Loại</th><th>Nhóm KT</th><th>Thao tác</th></tr></thead>
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
  document.getElementById('inp-khung-mon').value    = d.mon_hoc_id;
  document.getElementById('inp-khung-hk').value     = d.hoc_ky;
  document.getElementById('inp-khung-loai').value   = d.loai_mon;
  document.getElementById('inp-khung-nhom').value   = d.nhom_kien_thuc || '';
  document.getElementById('inp-khung-thutu').value  = d.thu_tu || 1;
  document.getElementById('inp-khung-ghichu').value = d.ghi_chu || '';
  document.getElementById('modal-khung-title').textContent = 'Sửa môn trong khung';
  Modal.open('modal-khung');
}

async function saveKhung() {
  const monId = Number(document.getElementById('inp-khung-mon').value);
  if (!monId) { Toast.warning('Vui lòng chọn môn học.'); return; }
  const body = {
    chuong_trinh_id: Number(_khungCtId),
    mon_hoc_id:      monId,
    hoc_ky:          Number(document.getElementById('inp-khung-hk').value),
    loai_mon:        document.getElementById('inp-khung-loai').value,
    nhom_kien_thuc:  document.getElementById('inp-khung-nhom').value,
    thu_tu:          Number(document.getElementById('inp-khung-thutu').value) || 1,
    ghi_chu:         document.getElementById('inp-khung-ghichu').value.trim(),
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