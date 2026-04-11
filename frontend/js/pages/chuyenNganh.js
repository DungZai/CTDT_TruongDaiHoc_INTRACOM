let _cnEditId = null, _cnNganh = [];
window._cnKeyword = '';
window._cnNganhId = null;

// ── Load có phân trang ─────────────────────────────────────
async function loadCN(page = 0, size = 10) {
  try {
    // Load dropdown ngành 1 lần
    if (!_cnNganh.length) {
      _cnNganh = await nganhApi.getAllForSelect();
      const opts = _cnNganh.map(n => `<option value="${n.id}">${n.tenNganh}</option>`).join('');
      document.getElementById('sel-cn-nganh-filter').innerHTML =
        '<option value="">-- Tất cả ngành --</option>' + opts;
      document.getElementById('sel-cn-nganh').innerHTML =
        '<option value="">-- Chọn ngành --</option>' + opts;
    }

    const params = new URLSearchParams({ page, size });
    if (window._cnKeyword) params.append('keyword', window._cnKeyword);
    if (window._cnNganhId) params.append('nganhId', window._cnNganhId);

    const res = await chuyenNganhApi.getAll(params.toString());

    renderTable({
      tbodyId: 'tbody-cn',
      columns: [
        { key: 'tenChuyenNganh' },
        { key: 'nganhId', render: r => _cnNganh.find(n => n.id === r.nganhId)?.tenNganh || '—' },
        { key: 'trangThai', render: r => Formatter.trangThai(r.trangThai) },
      ],
      data:       res.content,
      actions:    { edit: 'editCN', delete: 'deleteCN' },
      pageOffset: res.page * res.size,
    });

    Pagination.update('pagination-cn', res);
  } catch (e) {
        console.error('Chi tiết lỗi:', e);
    Toast.error('Không tải được dữ liệu chuyên ngành.');
  }
}

// ── Thêm mới ───────────────────────────────────────────────
function openCN() {
  _cnEditId = null;
  Modal.reset('modal-cn');
  document.getElementById('chk-cn-tt').checked = true;
  document.getElementById('modal-cn-title').textContent = 'Thêm chuyên ngành';
  Modal.open('modal-cn');
}

// ── Sửa ────────────────────────────────────────────────────
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

// ── Lưu ────────────────────────────────────────────────────
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
    _cnEditId
      ? await chuyenNganhApi.update(_cnEditId, body)
      : await chuyenNganhApi.create(body);
    Toast.success('Lưu thành công.');
    Modal.close('modal-cn');
    loadCN(Pagination.getPage('pagination-cn'), Pagination.getSize('pagination-cn'));
  } catch (e) { Toast.error(e.message); }
}

// ── Xóa ────────────────────────────────────────────────────
async function deleteCN(id) {
  if (!await confirmDelete('chuyên ngành này')) return;
  try {
    await chuyenNganhApi.delete(id);
    Toast.success('Xóa thành công.');
    const cur = Pagination.getPage('pagination-cn');
    loadCN(cur > 0 ? cur - 1 : 0, Pagination.getSize('pagination-cn'));
  } catch (e) { Toast.error(e.message); }
}