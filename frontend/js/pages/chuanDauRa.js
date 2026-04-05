let _cdrEditId = null, _cdrAll = [], _cdrCT = [], _cdrCtId = null;

async function initCDR() {
  _cdrCT = await chuongTrinhApi.getAll();
  const opts = _cdrCT.map(c => `<option value="${c.id}">${c.ten_chuong_trinh}</option>`).join('');
  document.getElementById('sel-cdr-ct').innerHTML = '<option value="">-- Chọn chương trình --</option>' + opts;
  document.getElementById('inp-cdr-ct').innerHTML = '<option value="">-- Chọn chương trình --</option>' + opts;
}

async function loadCDR(ctId) {
  _cdrCtId = ctId;
  if (!ctId) { renderTable({ tbodyId: 'tbody-cdr', columns: [], data: [] }); return; }
  _cdrAll = await chuanDauRaApi.getByChuongTrinh(ctId);
  renderTable({
    tbodyId: 'tbody-cdr',
    columns: [
      { key: 'ma_chuan' },
      { key: 'noi_dung', render: r => `<span title="${r.noi_dung}">${(r.noi_dung || '').slice(0, 80)}${r.noi_dung?.length > 80 ? '…' : ''}</span>` },
      { key: 'chuong_trinh_id', render: r => _cdrCT.find(c => c.id === r.chuong_trinh_id)?.ten_chuong_trinh || '—' },
    ],
    data: _cdrAll,
    actions: { edit: 'editCDR', delete: 'deleteCDR' },
  });
}

function openCDR() {
  _cdrEditId = null;
  Modal.reset('modal-cdr');
  if (_cdrCtId) document.getElementById('inp-cdr-ct').value = _cdrCtId;
  document.getElementById('modal-cdr-title').textContent = 'Thêm chuẩn đầu ra';
  Modal.open('modal-cdr');
}

async function editCDR(id) {
  _cdrEditId = id;
  const d = await chuanDauRaApi.getById(id);
  document.getElementById('inp-cdr-ct').value      = d.chuong_trinh_id;
  document.getElementById('inp-cdr-ma').value      = d.ma_chuan;
  document.getElementById('inp-cdr-noidung').value = d.noi_dung;
  document.getElementById('modal-cdr-title').textContent = 'Sửa chuẩn đầu ra';
  Modal.open('modal-cdr');
}

async function saveCDR() {
  const body = {
    chuong_trinh_id: Number(document.getElementById('inp-cdr-ct').value),
    ma_chuan:        document.getElementById('inp-cdr-ma').value.trim(),
    noi_dung:        document.getElementById('inp-cdr-noidung').value.trim(),
  };
  if (!body.chuong_trinh_id) { Toast.warning('Vui lòng chọn chương trình.'); return; }
  if (!Validator.required(body.ma_chuan, 'Mã chuẩn')) return;
  if (!Validator.required(body.noi_dung, 'Nội dung')) return;
  try {
    _cdrEditId ? await chuanDauRaApi.update(_cdrEditId, body) : await chuanDauRaApi.create(body);
    Toast.success('Lưu thành công.');
    Modal.close('modal-cdr');
    loadCDR(_cdrCtId);
  } catch (e) { Toast.error(e.message); }
}

async function deleteCDR(id) {
  if (!await confirmDelete('chuẩn đầu ra này')) return;
  try { await chuanDauRaApi.delete(id); Toast.success('Xóa thành công.'); loadCDR(_cdrCtId); }
  catch (e) { Toast.error(e.message); }
}