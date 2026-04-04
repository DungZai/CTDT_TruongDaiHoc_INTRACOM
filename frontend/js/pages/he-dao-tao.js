let allData = [], editingId = null, currentPage = 1;
const PAGE_SIZE = 10;

document.addEventListener('DOMContentLoaded', () => {
  if (!requireAuth()) return;
  renderUserInfo(); loadSidebar(); loadData();
});

async function loadData() {
  try {
    allData = await api.get('/api/he-dao-tao') || [];
    renderTable();
  } catch(e) { showToast(e.message,'error'); }
}

function renderTable() {
  const q = document.getElementById('search').value.toLowerCase();
  const filtered = q ? allData.filter(r => r.tenHe.toLowerCase().includes(q)) : allData;
  document.getElementById('tbl-count').textContent = `Tổng: ${filtered.length} hệ đào tạo`;
  const total = Math.ceil(filtered.length/PAGE_SIZE)||1;
  if (currentPage>total) currentPage=1;
  const rows = filtered.slice((currentPage-1)*PAGE_SIZE, currentPage*PAGE_SIZE);
  document.getElementById('tbl-body').innerHTML = rows.length
    ? rows.map(r=>`<tr>
        <td>${r.id}</td>
        <td><strong>${r.tenHe}</strong></td>
        <td>${r.thoiGianDaoTao} năm</td>
        <td>${r.tongTinChiMacDinh} tín chỉ</td>
        <td>${r.moTa||'—'}</td>
        <td><div class="action-btns">
          <button class="btn btn-primary btn-sm" onclick='openEdit(${JSON.stringify(r)})'>✏️ Sửa</button>
          <button class="btn btn-danger btn-sm" onclick="onDelete(${r.id})">🗑️ Xóa</button>
        </div></td>
      </tr>`).join('')
    : '<tr><td colspan="6" class="table-empty">Không có dữ liệu</td></tr>';
  renderPagination(total);
}

function filterRows() { currentPage=1; renderTable(); }
function renderPagination(total) {
  const pg = document.getElementById('pagination');
  if (total<=1){pg.innerHTML='';return;}
  let h=`<button ${currentPage===1?'disabled':''} onclick="goPage(${currentPage-1})">‹</button>`;
  for(let i=1;i<=total;i++) h+=`<button class="${i===currentPage?'active':''}" onclick="goPage(${i})">${i}</button>`;
  h+=`<button ${currentPage===total?'disabled':''} onclick="goPage(${currentPage+1})">›</button>`;
  pg.innerHTML=h;
}
function goPage(p){currentPage=p;renderTable();}

function formHtml(r) {
  return `
    <div class="form-group"><label>Tên hệ *</label>
      <input id="f-ten" value="${r?.tenHe||''}" placeholder="VD: Đại học chính quy"/></div>
    <div class="form-row">
      <div class="form-group"><label>Thời gian đào tạo (năm) *</label>
        <input id="f-tg" type="number" value="${r?.thoiGianDaoTao||4}" min="1"/></div>
      <div class="form-group"><label>Tổng tín chỉ mặc định *</label>
        <input id="f-tc" type="number" value="${r?.tongTinChiMacDinh||130}" min="1"/></div>
    </div>
    <div class="form-group"><label>Mô tả</label>
      <textarea id="f-mota">${r?.moTa||''}</textarea></div>`;
}

function openAdd() { editingId=null; openModal('➕ Thêm hệ đào tạo', formHtml(null), saveData); }
function openEdit(r) { editingId=r.id; openModal('✏️ Cập nhật hệ đào tạo', formHtml(r), saveData); }

async function saveData() {
  const body = { tenHe:val('f-ten'), thoiGianDaoTao:+val('f-tg'), tongTinChiMacDinh:+val('f-tc'), moTa:val('f-mota') };
  if (!body.tenHe) { showToast('Vui lòng nhập tên hệ','error'); return; }
  try {
    editingId ? await api.put(`/api/he-dao-tao/${editingId}`,body) : await api.post('/api/he-dao-tao',body);
    showToast(editingId?'Cập nhật thành công':'Thêm thành công');
    closeModal(); loadData();
  } catch(e) { showToast(e.message,'error'); }
}

async function onDelete(id) {
  if (!confirm('Xác nhận xóa?')) return;
  try { await api.delete(`/api/he-dao-tao/${id}`); showToast('Xóa thành công'); loadData(); }
  catch(e) { showToast(e.message,'error'); }
}