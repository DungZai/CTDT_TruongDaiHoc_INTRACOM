let _nganhEditId = null;

async function loadNganh() {
  const data = await nganhApi.getAll();
  renderTable({
    tbodyId: 'tbody-nganh',
    columns: [
      { key: 'maNganh' },
      { key: 'tenNganh' },
      { key: 'trangThai', render: r => Formatter.trangThai(r.trangThai) },
    ],
    data,
    actions: { edit: 'editNganh', delete: 'deleteNganh' },
    // không truyền type → giảng viên không thấy thao tác
  });
}

function openNganh() {
  _nganhEditId = null;
  Modal.reset('modal-nganh');
  document.getElementById('chk-nganh-tt').checked = true;
  document.getElementById('modal-nganh-title').textContent = 'Thêm ngành';
  Modal.open('modal-nganh');
}

async function editNganh(id) {
  _nganhEditId = id;
  const d = await nganhApi.getById(id);
  document.getElementById('inp-ma-nganh').value   = d.maNganh;
  document.getElementById('inp-ten-nganh').value  = d.tenNganh;
  document.getElementById('inp-nganh-mota').value = d.moTa || '';
  document.getElementById('chk-nganh-tt').checked = d.trangThai;
  document.getElementById('modal-nganh-title').textContent = 'Sửa ngành';
  Modal.open('modal-nganh');
}

async function saveNganh() {
  const body = {
    maNganh:   document.getElementById('inp-ma-nganh').value.trim(),
    tenNganh:  document.getElementById('inp-ten-nganh').value.trim(),
    moTa:      document.getElementById('inp-nganh-mota').value.trim(),
    trangThai: document.getElementById('chk-nganh-tt').checked,
  };
  if (!Validator.required(body.maNganh, 'Mã ngành')) return;
  if (!Validator.required(body.tenNganh, 'Tên ngành')) return;
  try {
    _nganhEditId ? await nganhApi.update(_nganhEditId, body) : await nganhApi.create(body);
    Toast.success('Lưu thành công.');
    Modal.close('modal-nganh');
    loadNganh();
  } catch (e) { Toast.error(e.message); }
}

async function deleteNganh(id) {
  if (!await confirmDelete('ngành này')) return;
  try { await nganhApi.delete(id); Toast.success('Xóa thành công.'); loadNganh(); }
  catch (e) { Toast.error(e.message); }
}