let allData = [], editingId = null;
const PAGE_SIZE = 10;
let currentPage = 1;

document.addEventListener('DOMContentLoaded', () => {
  if (!requireAuth()) return;
  renderUserInfo();
  loadSidebar();
  loadData();
});

async function loadData() {
  try {
    allData = await api.get('/api/nganh') || [];
    renderTable();
  } catch(e) { showToast(e.message, 'error'); }
}

function renderTable() {
  const q = document.getElementById('search').value.toLowerCase();
  const filtered = q
    ? allData.filter(r => (r.maNganh+r.tenNganh+r.moTa).toLowerCase().includes(q))
    : allData;

  document.getElementById('tbl-count').textContent = `Tổng: ${filtered.length} ngành`;
  const totalPage = Math.ceil(filtered.length / PAGE_SIZE) || 1;
  if (currentPage > totalPage) currentPage = 1;
  const rows = filtered.slice((currentPage-1)*PAGE_SIZE, currentPage*PAGE_SIZE);

  const tb = document.getElementById('tbl-body');
  tb.innerHTML = rows.length
    ? rows.map(r => `<tr>
        <td>${r.id}</td>
        <td><strong>${r.maNganh}</strong></td>
        <td>${r.tenNganh}</td>
        <td style="max-width:220px;overflow:hidden;text-overflow:ellipsis;white-space:nowrap">${r.moTa||'—'}</td>
        <td><span class="badge ${r.trangThai?'badge-green':'badge-red'}">${r.trangThai?'Hoạt động':'Ngừng'}</span></td>
        <td>
          <div class="action-btns">
            <button class="btn btn-primary btn-sm" onclick='openEdit(${JSON.stringify(r)})'>✏️ Sửa</button>
            <button class="btn btn-danger btn-sm" onclick="onDelete(${r.id})">🗑️ Xóa</button>
          </div>
        </td>
      </tr>`).join('')
    : '<tr><td colspan="6" class="table-empty">Không có dữ liệu</td></tr>';

  renderPagination(totalPage);
}

function filterRows() { currentPage = 1; renderTable(); }

function renderPagination(total) {
  const pg = document.getElementById('pagination');
  if (total <= 1) { pg.innerHTML=''; return; }
  let html = `<button ${currentPage===1?'disabled':''} onclick="goPage(${currentPage-1})">‹</button>`;
  for (let i=1; i<=total; i++)
    html += `<button class="${i===currentPage?'active':''}" onclick="goPage(${i})">${i}</button>`;
  html += `<button ${currentPage===total?'disabled':''} onclick="goPage(${currentPage+1})">›</button>`;
  pg.innerHTML = html;
}
function goPage(p) { currentPage=p; renderTable(); }

function formHtml(r) {
  return `
    <div class="form-group"><label>Mã ngành *</label>
      <input id="f-ma" value="${r?.maNganh||''}" placeholder="VD: CNTT"/></div>
    <div class="form-group"><label>Tên ngành *</label>
      <input id="f-ten" value="${r?.tenNganh||''}" placeholder="VD: Công nghệ thông tin"/></div>
    <div class="form-group"><label>Mô tả</label>
      <textarea id="f-mota">${r?.moTa||''}</textarea></div>
    <div class="form-group"><label>Trạng thái</label>
      ${makeToggle('f-tt', r?.trangThai!==false)}</div>`;
}

function openAdd() {
  editingId = null;
  openModal('➕ Thêm ngành mới', formHtml(null), saveData);
}
function openEdit(r) {
  editingId = r.id;
  openModal('✏️ Cập nhật ngành', formHtml(r), saveData);
}

async function saveData() {
  const body = {
    maNganh:   val('f-ma'),
    tenNganh:  val('f-ten'),
    moTa:      val('f-mota'),
    trangThai: getToggle('f-tt')
  };
  if (!body.maNganh || !body.tenNganh) { showToast('Vui lòng nhập đủ thông tin bắt buộc','error'); return; }
  try {
    editingId
      ? await api.put(`/api/nganh/${editingId}`, body)
      : await api.post('/api/nganh', body);
    showToast(editingId ? 'Cập nhật thành công' : 'Thêm thành công');
    closeModal(); loadData();
  } catch(e) { showToast(e.message,'error'); }
}

async function onDelete(id) {
  if (!confirm('Bạn có chắc muốn xóa ngành này?')) return;
  try {
    await api.delete(`/api/nganh/${id}`);
    showToast('Xóa thành công');
    loadData();
  } catch(e) { showToast(e.message,'error'); }
}