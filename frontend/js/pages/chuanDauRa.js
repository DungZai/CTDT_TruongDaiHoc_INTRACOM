let _cdrEditId = null, _cdrAll = [], _cdrCT = [], _cdrCtId = null;

async function initCDR() {
  _cdrCT = await chuongTrinhApi.getAll();
  const opts = _cdrCT.map(c => `<option value="${c.id}">${c.tenChuongTrinh}</option>`).join('');
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
      { key: 'maChuan' },
      { key: 'noiDung', render: r => `<span title="${r.noiDung}">${(r.noiDung || '').slice(0, 80)}${r.noiDung?.length > 80 ? '…' : ''}</span>` },
      { key: 'chuongTrinhId', render: r => _cdrCT.find(c => c.id === r.chuongTrinhId)?.tenChuongTrinh || '—' },
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
  document.getElementById('inp-cdr-ct').value      = d.chuongTrinhId;
  document.getElementById('inp-cdr-ma').value      = d.maChuan;
  document.getElementById('inp-cdr-noidung').value = d.noiDung;
  document.getElementById('modal-cdr-title').textContent = 'Sửa chuẩn đầu ra';
  Modal.open('modal-cdr');
}

async function saveCDR() {
  const body = {
    chuongTrinhId: Number(document.getElementById('inp-cdr-ct').value),
    maChuan:       document.getElementById('inp-cdr-ma').value.trim(),
    noiDung:       document.getElementById('inp-cdr-noidung').value.trim(),
  };
  if (!body.chuongTrinhId) { Toast.warning('Vui lòng chọn chương trình.'); return; }
  if (!Validator.required(body.maChuan, 'Mã chuẩn')) return;
  if (!Validator.required(body.noiDung, 'Nội dung')) return;
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