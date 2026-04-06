let _ctEditId = null, _ctAll = [], _ctNganh = [], _ctHe = [];

async function loadCT() {
  [_ctAll, _ctNganh, _ctHe] = await Promise.all([
    chuongTrinhApi.getAll(), nganhApi.getAll(), heDaoTaoApi.getAll(),
  ]);
  const nOpts = _ctNganh.map(n => `<option value="${n.id}">${n.tenNganh}</option>`).join('');
  const hOpts = _ctHe.map(h => `<option value="${h.id}">${h.tenHe}</option>`).join('');
  document.getElementById('sel-ct-nganh-filter').innerHTML = '<option value="">-- Tất cả ngành --</option>' + nOpts;
  document.getElementById('sel-ct-he-filter').innerHTML   = '<option value="">-- Tất cả hệ --</option>' + hOpts;
  document.getElementById('inp-ct-nganh').innerHTML       = '<option value="">-- Chọn ngành --</option>' + nOpts;
  document.getElementById('inp-ct-he').innerHTML          = '<option value="">-- Chọn hệ --</option>' + hOpts;
  renderCT(_ctAll);
}

function renderCT(data) {
  renderTable({
    tbodyId: 'tbody-ct',
    columns: [
      { key: 'tenChuongTrinh' },
      { key: 'nganhId',      render: r => _ctNganh.find(n => n.id === r.nganhId)?.tenNganh || '—' },
      { key: 'heId',         render: r => _ctHe.find(h => h.id === r.heId)?.tenHe || '—' },
      { key: 'tongTinChi',   render: r => `${r.tongTinChi} TC` },
      { key: 'namPhatHanh' },
      { key: 'trangThai',    render: r => Formatter.trangThai(r.trangThai) },
    ],
    data,
    actions: { edit: 'editCT', delete: 'deleteCT' },
  });
}

function filterCT() {
  const ng = Number(document.getElementById('sel-ct-nganh-filter').value);
  const he = Number(document.getElementById('sel-ct-he-filter').value);
  renderCT(_ctAll.filter(d => (!ng || d.nganhId === ng) && (!he || d.heId === he)));
}

function openCT() {
  _ctEditId = null;
  Modal.reset('modal-ct');
  document.getElementById('chk-ct-tt').checked = true;
  document.getElementById('inp-ct-nam').value = new Date().getFullYear();
  document.getElementById('modal-ct-title').textContent = 'Thêm chương trình';
  Modal.open('modal-ct');
}

async function editCT(id) {
  _ctEditId = id;
  const d = await chuongTrinhApi.getById(id);
  document.getElementById('inp-ten-ct').value   = d.tenChuongTrinh;
  document.getElementById('inp-ct-nganh').value = d.nganhId;
  document.getElementById('inp-ct-he').value    = d.heId;
  document.getElementById('inp-ct-tc').value    = d.tongTinChi;
  document.getElementById('inp-ct-nam').value   = d.namPhatHanh;
  document.getElementById('inp-ct-mota').value  = d.moTa || '';
  document.getElementById('chk-ct-tt').checked  = d.trangThai;
  document.getElementById('modal-ct-title').textContent = 'Sửa chương trình';
  Modal.open('modal-ct');
}

async function saveCT() {
  const body = {
    tenChuongTrinh: document.getElementById('inp-ten-ct').value.trim(),
    nganhId:        Number(document.getElementById('inp-ct-nganh').value),
    heId:           Number(document.getElementById('inp-ct-he').value),
    tongTinChi:     Number(document.getElementById('inp-ct-tc').value),
    namPhatHanh:    Number(document.getElementById('inp-ct-nam').value),
    moTa:           document.getElementById('inp-ct-mota').value.trim(),
    trangThai:      document.getElementById('chk-ct-tt').checked,
  };
  if (!Validator.required(body.tenChuongTrinh, 'Tên chương trình')) return;
  if (!body.nganhId)   { Toast.warning('Vui lòng chọn ngành.'); return; }
  if (!body.heId)      { Toast.warning('Vui lòng chọn hệ.'); return; }
  if (!Validator.positiveInt(body.tongTinChi,  'Tổng tín chỉ')) return;
  if (!Validator.positiveInt(body.namPhatHanh, 'Năm phát hành')) return;
  try {
    _ctEditId ? await chuongTrinhApi.update(_ctEditId, body) : await chuongTrinhApi.create(body);
    Toast.success('Lưu thành công.');
    Modal.close('modal-ct');
    loadCT();
  } catch (e) { Toast.error(e.message); }
}

async function deleteCT(id) {
  if (!await confirmDelete('chương trình này')) return;
  try { await chuongTrinhApi.delete(id); Toast.success('Xóa thành công.'); loadCT(); }
  catch (e) { Toast.error(e.message); }
}