let _cnEditId = null, _cnAll = [], _cnNganh = [];

async function loadCN() {
  [_cnAll, _cnNganh] = await Promise.all([chuyenNganhApi.getAll(), nganhApi.getAll()]);
  const opts = _cnNganh.map(n => `<option value="${n.id}">${n.tenNganh}</option>`).join('');
  document.getElementById('sel-cn-nganh-filter').innerHTML = '<option value="">-- Tất cả ngành --</option>' + opts;
  document.getElementById('sel-cn-nganh').innerHTML        = '<option value="">-- Chọn ngành --</option>' + opts;
  renderCN(_cnAll);
}

function renderCN(data) {
  renderTable({
    tbodyId: 'tbody-cn',
    columns: [
      { key: 'tenChuyenNganh' },
      { key: 'nganhId', render: r => _cnNganh.find(n => n.id === r.nganhId)?.tenNganh || '—' },
      { key: 'trangThai', render: r => Formatter.trangThai(r.trangThai) },
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
  document.getElementById('sel-cn-nganh').value = d.nganhId;
  document.getElementById('inp-ten-cn').value   = d.tenChuyenNganh;
  document.getElementById('inp-cn-mota').value  = d.moTa || '';
  document.getElementById('chk-cn-tt').checked  = d.trangThai;
  document.getElementById('modal-cn-title').textContent = 'Sửa chuyên ngành';
  Modal.open('modal-cn');
}

async function saveCN() {
  const ngId = Number(document.getElementById('sel-cn-nganh').value);
  const ten  = document.getElementById('inp-ten-cn').value.trim();
  if (!ngId) { Toast.warning('Vui lòng chọn ngành.'); return; }
  if (!Validator.required(ten, 'Tên chuyên ngành')) return;
  const body = {
    nganhId:        ngId,
    tenChuyenNganh: ten,
    moTa:           document.getElementById('inp-cn-mota').value.trim(),
    trangThai:      document.getElementById('chk-cn-tt').checked,
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