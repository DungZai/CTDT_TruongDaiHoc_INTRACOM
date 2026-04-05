let _ctEditId = null, _ctAll = [], _ctNganh = [], _ctHe = [];

async function loadCT() {
  [_ctAll, _ctNganh, _ctHe] = await Promise.all([
    chuongTrinhApi.getAll(), nganhApi.getAll(), heDaoTaoApi.getAll(),
  ]);
  const nOpts = _ctNganh.map(n => `<option value="${n.id}">${n.ten_nganh}</option>`).join('');
  const hOpts = _ctHe.map(h => `<option value="${h.id}">${h.ten_he}</option>`).join('');
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
      { key: 'ten_chuong_trinh' },
      { key: 'nganh_id', render: r => _ctNganh.find(n => n.id === r.nganh_id)?.ten_nganh || '—' },
      { key: 'he_id',    render: r => _ctHe.find(h => h.id === r.he_id)?.ten_he || '—' },
      { key: 'tong_tin_chi',  render: r => `${r.tong_tin_chi} TC` },
      { key: 'nam_phat_hanh' },
      { key: 'trang_thai', render: r => Formatter.trangThai(r.trang_thai) },
    ],
    data,
    actions: { edit: 'editCT', delete: 'deleteCT' },
  });
}

function filterCT() {
  const ng = Number(document.getElementById('sel-ct-nganh-filter').value);
  const he = Number(document.getElementById('sel-ct-he-filter').value);
  renderCT(_ctAll.filter(d => (!ng || d.nganh_id === ng) && (!he || d.he_id === he)));
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
  document.getElementById('inp-ten-ct').value   = d.ten_chuong_trinh;
  document.getElementById('inp-ct-nganh').value = d.nganh_id;
  document.getElementById('inp-ct-he').value    = d.he_id;
  document.getElementById('inp-ct-tc').value    = d.tong_tin_chi;
  document.getElementById('inp-ct-nam').value   = d.nam_phat_hanh;
  document.getElementById('inp-ct-mota').value  = d.mo_ta || '';
  document.getElementById('chk-ct-tt').checked  = d.trang_thai;
  document.getElementById('modal-ct-title').textContent = 'Sửa chương trình';
  Modal.open('modal-ct');
}

async function saveCT() {
  const body = {
    ten_chuong_trinh: document.getElementById('inp-ten-ct').value.trim(),
    nganh_id:  Number(document.getElementById('inp-ct-nganh').value),
    he_id:     Number(document.getElementById('inp-ct-he').value),
    tong_tin_chi:  Number(document.getElementById('inp-ct-tc').value),
    nam_phat_hanh: Number(document.getElementById('inp-ct-nam').value),
    mo_ta:     document.getElementById('inp-ct-mota').value.trim(),
    trang_thai:document.getElementById('chk-ct-tt').checked,
  };
  if (!Validator.required(body.ten_chuong_trinh, 'Tên chương trình')) return;
  if (!body.nganh_id) { Toast.warning('Vui lòng chọn ngành.'); return; }
  if (!body.he_id)    { Toast.warning('Vui lòng chọn hệ.'); return; }
  if (!Validator.positiveInt(body.tong_tin_chi, 'Tổng tín chỉ')) return;
  if (!Validator.positiveInt(body.nam_phat_hanh, 'Năm phát hành')) return;
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