let _khungCtId = null, _khungEditId = null, _khungMons = [];
let _khungData = [], _khungActiveHK = null;

async function initKhung() {
  const cts = await chuongTrinhApi.getAllForSelect();
  document.getElementById('sel-khung-ct').innerHTML =
    '<option value="">-- Chọn chương trình --</option>'
    + cts.map(c => `<option value="${c.id}">${c.tenChuongTrinh}</option>`).join('');
}

async function loadKhung(ctId) {
  _khungCtId = ctId;
  const emptyWrap = document.getElementById('wrap-khung-empty');
  const tabsWrap  = document.getElementById('hk-tabs-wrap');
  const isAdmin   = TokenService.getRole() === 'ADMIN';

  if (!ctId) {
    emptyWrap.style.display = '';
    tabsWrap.style.display  = 'none';
    emptyWrap.innerHTML = '<p class="text-muted mt-3">Chọn chương trình để xem khung.</p>';
    return;
  }

  const [khung, mons] = await Promise.all([
    khungApi.getByChuongTrinh(ctId),
    monHocApi.getAllForSelect(),
  ]);

  _khungMons = mons;
  _khungData = khung;

  // Dropdown môn học trong modal
  document.getElementById('inp-khung-mon').innerHTML =
    '<option value="">-- Chọn môn --</option>'
    + mons.map(m => `<option value="${m.id}">${m.maMon} — ${m.tenMon} (${m.tinChi}TC)</option>`).join('');

  // Tổng TC + số môn
  const tongTC = khung.reduce((s, k) => s + (mons.find(m => m.id === k.monHocId)?.tinChi || 0), 0);
  document.getElementById('lbl-tong-tc').textContent = `📚 Tổng: ${tongTC} TC`;
  document.getElementById('lbl-so-mon').textContent  = `📋 ${khung.length} môn`;

  if (!khung.length) {
    emptyWrap.style.display = '';
    tabsWrap.style.display  = 'none';
    emptyWrap.innerHTML = `
      <div style="text-align:center;padding:48px 24px;color:#94a3b8">
        <div style="font-size:40px;margin-bottom:8px">📭</div>
        <div>Chưa có môn nào trong khung.</div>
        ${isAdmin ? '<button class="btn btn-primary btn-sm mt-3" onclick="openKhung()">+ Thêm môn đầu tiên</button>' : ''}
      </div>`;
    return;
  }

  emptyWrap.style.display = 'none';
  tabsWrap.style.display  = '';

  // Nhóm theo học kỳ
  const byHK = {};
  khung.forEach(k => { (byHK[k.hocKy] || (byHK[k.hocKy] = [])).push(k); });
  const hocKys = Object.keys(byHK).map(Number).sort((a, b) => a - b);

  // Giữ tab đang chọn nếu vẫn còn, không thì chọn tab đầu
  if (!_khungActiveHK || !byHK[_khungActiveHK]) _khungActiveHK = hocKys[0];

  // Render tabs
  const tabsEl = document.getElementById('hk-tabs');
  tabsEl.innerHTML = hocKys.map(hk => {
    const tcHK = byHK[hk].reduce((s, k) => s + (mons.find(m => m.id === k.monHocId)?.tinChi || 0), 0);
    return `<button class="hk-tab ${hk === _khungActiveHK ? 'active' : ''}"
      onclick="switchHK(${hk})">
      Học kỳ ${hk}
      <span class="tc-badge">${tcHK} TC</span>
    </button>`;
  }).join('');

  _renderHK(_khungActiveHK, byHK[_khungActiveHK], mons, isAdmin);
}

function switchHK(hk) {
  _khungActiveHK = hk;

  // Update tab active
  document.querySelectorAll('.hk-tab').forEach(btn => {
    btn.classList.toggle('active', Number(btn.textContent.match(/\d+/)?.[0]) === hk
      || btn.onclick?.toString().includes(`switchHK(${hk})`));
  });
  // Re-render đúng tab
  document.querySelectorAll('.hk-tab').forEach(btn => {
    const match = btn.getAttribute('onclick')?.match(/switchHK\((\d+)\)/);
    if (match) btn.classList.toggle('active', Number(match[1]) === hk);
  });

  const byHK = {};
  _khungData.forEach(k => { (byHK[k.hocKy] || (byHK[k.hocKy] = [])).push(k); });
  _renderHK(hk, byHK[hk] || [], _khungMons, TokenService.getRole() === 'ADMIN');
}

function _renderHK(hk, items, mons, isAdmin) {
  const wrap = document.getElementById('wrap-khung-table');
  if (!items || !items.length) {
    wrap.innerHTML = '<div class="khung-empty">Không có môn nào trong học kỳ này.</div>';
    return;
  }

  const sorted = items.slice().sort((a, b) => a.thuTu - b.thuTu);
  const rows = sorted.map((k, i) => {
    const m = mons.find(x => x.id === k.monHocId);
    const loaiClass = k.loaiMon === 'Bắt buộc' ? 'tag-bb' : k.loaiMon === 'Tự chọn' ? 'tag-tc' : 'tag-tn';
    const actions = isAdmin ? `
      <button class="btn btn-sm btn-warning me-1" onclick="editKhung(${k.id})">Sửa</button>
      <button class="btn btn-sm btn-danger" onclick="deleteKhung(${k.id})">Xóa</button>
    ` : '';
    return `<tr>
      <td style="color:#94a3b8;font-size:12px;text-align:center;width:36px">${i + 1}</td>
      <td class="td-ma">${m?.maMon || '—'}</td>
      <td class="td-ten">${m?.tenMon || '—'}</td>
      <td class="td-tc">${m?.tinChi || '—'}</td>
      <td><span class="tag-loai ${loaiClass}">${k.loaiMon}</span></td>
      <td><span class="tag-nhom">${k.nhomKienThuc || '—'}</span></td>
      ${isAdmin ? `<td class="khung-actions">${actions}</td>` : ''}
    </tr>`;
  }).join('');

  const tongHK = items.reduce((s, k) => s + (mons.find(m => m.id === k.monHocId)?.tinChi || 0), 0);

  wrap.innerHTML = `
    <div class="khung-table-wrap">
      <table class="khung-table">
        <thead>
          <tr>
            <th style="width:36px">#</th>
            <th>Mã môn</th>
            <th>Tên môn học</th>
            <th style="text-align:center">TC</th>
            <th>Loại</th>
            <th>Nhóm KT</th>
            ${isAdmin ? '<th></th>' : ''}
          </tr>
        </thead>
        <tbody>${rows}</tbody>
      </table>
      <div class="khung-summary">
        <span>${items.length} môn học</span>
        <span>Tổng: <strong>${tongHK} tín chỉ</strong></span>
      </div>
    </div>
  `;
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
    chuongTrinhId: Number(_khungCtId),
    monHocId:      monId,
    hocKy:         Number(document.getElementById('inp-khung-hk').value),
    loaiMon:       document.getElementById('inp-khung-loai').value,
    nhomKienThuc:  document.getElementById('inp-khung-nhom').value,
    thuTu:         Number(document.getElementById('inp-khung-thutu').value) || 1,
    ghiChu:        document.getElementById('inp-khung-ghichu').value.trim(),
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