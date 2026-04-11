// =============================================
// monHoc.js — Có phân trang + tìm kiếm API
// =============================================

let _monEditId  = null;
window._monKeyword = '';

// ── Load dữ liệu có phân trang ─────────────────
async function loadMon(page = 0, size = 10) {
  try {
    const params = new URLSearchParams({ page, size });
    if (window._monKeyword) params.append('keyword', window._monKeyword);

    const res = await monHocApi.getAll(params.toString());
    // res = { content, page, size, totalElements, totalPages, first, last }

    renderTable({
      tbodyId: 'tbody-mon',
      columns: [
        { key: 'maMon' },
        { key: 'tenMon' },
        { key: 'tinChi',    render: r => `${r.tinChi} TC` },
        { key: 'soTietLt',  render: r => r.soTietLt ?? '—' },
        { key: 'soTietTh',  render: r => r.soTietTh ?? '—' },
        { key: 'trangThai', render: r => Formatter.trangThai(r.trangThai) },
      ],
      data:       res.content,
      actions:    { edit: 'editMon', delete: 'deleteMon' },
      pageOffset: res.page * res.size,
    });

    Pagination.update('pagination-mon', res);

  } catch (e) {
    Toast.error('Không tải được dữ liệu môn học.');
  }
}

// ── Thêm mới ───────────────────────────────────
function openMon() {
  _monEditId = null;
  Modal.reset('modal-mon');
  document.getElementById('chk-mon-tt').checked = true;
  document.getElementById('modal-mon-title').textContent = 'Thêm môn học';
  Modal.open('modal-mon');
}

// ── Sửa ────────────────────────────────────────
async function editMon(id) {
  _monEditId = id;
  const d = await monHocApi.getById(id);
  document.getElementById('inp-ma-mon').value   = d.maMon;
  document.getElementById('inp-ten-mon').value  = d.tenMon;
  document.getElementById('inp-mon-tc').value   = d.tinChi;
  document.getElementById('inp-mon-lt').value   = d.soTietLt ?? '';
  document.getElementById('inp-mon-th').value   = d.soTietTh ?? '';
  document.getElementById('inp-mon-mota').value = d.moTa || '';
  document.getElementById('chk-mon-tt').checked = d.trangThai;
  document.getElementById('modal-mon-title').textContent = 'Sửa môn học';
  Modal.open('modal-mon');
}

// ── Lưu (thêm/sửa) ─────────────────────────────
async function saveMon() {
  const body = {
    maMon:     document.getElementById('inp-ma-mon').value.trim(),
    tenMon:    document.getElementById('inp-ten-mon').value.trim(),
    tinChi:    Number(document.getElementById('inp-mon-tc').value),
    soTietLt:  Number(document.getElementById('inp-mon-lt').value) || 0,
    soTietTh:  Number(document.getElementById('inp-mon-th').value) || 0,
    moTa:      document.getElementById('inp-mon-mota').value.trim(),
    trangThai: document.getElementById('chk-mon-tt').checked,
  };
  if (!Validator.required(body.maMon,  'Mã môn'))   return;
  if (!Validator.required(body.tenMon, 'Tên môn'))  return;
  if (!Validator.positiveInt(body.tinChi, 'Tín chỉ')) return;
  try {
    _monEditId
      ? await monHocApi.update(_monEditId, body)
      : await monHocApi.create(body);
    Toast.success('Lưu thành công.');
    Modal.close('modal-mon');
    // Reload trang hiện tại
    loadMon(Pagination.getPage('pagination-mon'), Pagination.getSize('pagination-mon'));
  } catch (e) { Toast.error(e.message); }
}

// ── Xóa ────────────────────────────────────────
async function deleteMon(id) {
  if (!await confirmDelete('môn học này')) return;
  try {
    await monHocApi.delete(id);
    Toast.success('Xóa thành công.');
    // Nếu xóa item cuối trang → lùi 1 trang
    const cur = Pagination.getPage('pagination-mon');
    loadMon(cur > 0 ? cur - 1 : 0, Pagination.getSize('pagination-mon'));
  } catch (e) { Toast.error(e.message); }
}
