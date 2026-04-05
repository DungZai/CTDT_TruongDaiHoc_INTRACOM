let _monEditId = null;

async function loadMon() {
  const data = await monHocApi.getAll();
  renderTable({
    tbodyId: 'tbody-mon',
    columns: [
      { key: 'ma_mon' },
      { key: 'ten_mon' },
      { key: 'tin_chi',    render: r => `${r.tin_chi} TC` },
      { key: 'so_tiet_lt', render: r => r.so_tiet_lt ?? '—' },
      { key: 'so_tiet_th', render: r => r.so_tiet_th ?? '—' },
      { key: 'trang_thai', render: r => Formatter.trangThai(r.trang_thai) },
    ],
    data,
    actions: { edit: 'editMon', delete: 'deleteMon' },
  });
}

function openMon() {
  _monEditId = null;
  Modal.reset('modal-mon');
  document.getElementById('chk-mon-tt').checked = true;
  document.getElementById('modal-mon-title').textContent = 'Thêm môn học';
  Modal.open('modal-mon');
}

async function editMon(id) {
  _monEditId = id;
  const d = await monHocApi.getById(id);
  document.getElementById('inp-ma-mon').value   = d.ma_mon;
  document.getElementById('inp-ten-mon').value  = d.ten_mon;
  document.getElementById('inp-mon-tc').value   = d.tin_chi;
  document.getElementById('inp-mon-lt').value   = d.so_tiet_lt ?? '';
  document.getElementById('inp-mon-th').value   = d.so_tiet_th ?? '';
  document.getElementById('inp-mon-mota').value = d.mo_ta || '';
  document.getElementById('chk-mon-tt').checked = d.trang_thai;
  document.getElementById('modal-mon-title').textContent = 'Sửa môn học';
  Modal.open('modal-mon');
}

async function saveMon() {
  const body = {
    ma_mon:     document.getElementById('inp-ma-mon').value.trim(),
    ten_mon:    document.getElementById('inp-ten-mon').value.trim(),
    tin_chi:    Number(document.getElementById('inp-mon-tc').value),
    so_tiet_lt: Number(document.getElementById('inp-mon-lt').value) || 0,
    so_tiet_th: Number(document.getElementById('inp-mon-th').value) || 0,
    mo_ta:      document.getElementById('inp-mon-mota').value.trim(),
    trang_thai: document.getElementById('chk-mon-tt').checked,
  };
  if (!Validator.required(body.ma_mon, 'Mã môn')) return;
  if (!Validator.required(body.ten_mon, 'Tên môn')) return;
  if (!Validator.positiveInt(body.tin_chi, 'Tín chỉ')) return;
  try {
    _monEditId ? await monHocApi.update(_monEditId, body) : await monHocApi.create(body);
    Toast.success('Lưu thành công.');
    Modal.close('modal-mon');
    loadMon();
  } catch (e) { Toast.error(e.message); }
}

async function deleteMon(id) {
  if (!await confirmDelete('môn học này')) return;
  try { await monHocApi.delete(id); Toast.success('Xóa thành công.'); loadMon(); }
  catch (e) { Toast.error(e.message); }
}