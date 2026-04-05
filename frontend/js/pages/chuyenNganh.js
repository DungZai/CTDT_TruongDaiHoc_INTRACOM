let _cnEditId = null, _cnAll = [], _cnNganh = [];

async function loadCN() {
  [_cnAll, _cnNganh] = await Promise.all([chuyenNganhApi.getAll(), nganhApi.getAll()]);
  const opts = _cnNganh.map(n => `<option value="${n.id}">${n.ten_nganh}</option>`).join('');
  document.getElementById('sel-cn-nganh-filter').innerHTML = '<option value="">-- Tất cả ngành --</option>' + opts;
  document.getElementById('sel-cn-nganh').innerHTML        = '<option value="">-- Chọn ngành --</option>' + opts;
  renderCN(_cnAll);
}

function renderCN(data) {
  renderTable({
    tbodyId: 'tbody-cn',
    columns: [
      { key: 'ten_chuyen_nganh' },
      { key: 'nganh_id', render: r => _cnNganh.find(n => n.id === r.nganh_id)?.ten_nganh || '—' },
      { key: 'trang_thai', render: r => Formatter.trangThai(r.trang_thai) },
    ],
    data,
    actions: { edit: 'editCN', delete: 'deleteCN' },
  });
}

function openCN() {
  _cnEditId = null;
  Modal.reset('modal-cn');
  document.getElementById('chk-cn-tt').checked = true;
  document.getElementById('modal-cn-title').textContent = 'Thêm chuyên ngành';
  Modal.open('modal-cn');
}

async function editCN(id) {
  _cnEditId = id;
  const d = await chuyenNganhApi.getById(id);
  document.getElementById('sel-cn-nganh').value   = d.nganh_id;
  document.getElementById('inp-ten-cn').value     = d.ten_chuyen_nganh;
  document.getElementById('inp-cn-mota').value    = d.mo_ta || '';
  document.getElementById('chk-cn-tt').checked    = d.trang_thai;
  document.getElementById('modal-cn-title').textContent = 'Sửa chuyên ngành';
  Modal.open('modal-cn');
}

async function saveCN() {
  const ngId = Number(document.getElementById('sel-cn-nganh').value);
  const ten  = document.getElementById('inp-ten-cn').value.trim();
  if (!ngId) { Toast.warning('Vui lòng chọn ngành.'); return; }
  if (!Validator.required(ten, 'Tên chuyên ngành')) return;
  const body = {
    nganh_id: ngId, ten_chuyen_nganh: ten,
    mo_ta: document.getElementById('inp-cn-mota').value.trim(),
    trang_thai: document.getElementById('chk-cn-tt').checked,
  };
  try {
    _cnEditId ? await chuyenNganhApi.update(_cnEditId, body) : await chuyenNganhApi.create(body);
    Toast.success('Lưu thành công.');
    Modal.close('modal-cn');
    loadCN();
  } catch (e) { Toast.error(e.message); }
}

async function deleteCN(id) {
  if (!await confirmDelete('chuyên ngành này')) return;
  try { await chuyenNganhApi.delete(id); Toast.success('Xóa thành công.'); loadCN(); }
  catch (e) { Toast.error(e.message); }
}